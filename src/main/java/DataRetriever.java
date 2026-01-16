import EnumForIngredient.CategoryEnum;
import EnumForIngredient.DishTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // Instance de connexion pour éviter le static si nécessaire,
    // ou appel direct à la méthode d'instance de DBConnection.
    private final DBConnection dbConnection = new DBConnection();

    /**
     * a) Récupère un plat et tous ses ingrédients associés.
     */
    public Dish findDishById(Integer id) {
        String dishSql = "SELECT * FROM dish WHERE id = ?";
        String ingredientSql = "SELECT * FROM ingredient WHERE id_dish = ?";
        Dish dish = null;

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement psDish = conn.prepareStatement(dishSql);
             PreparedStatement psIng = conn.prepareStatement(ingredientSql)) {

            psDish.setInt(1, id);
            try (ResultSet rsDish = psDish.executeQuery()) {
                if (rsDish.next()) {
                    dish = new Dish();
                    dish.setId(rsDish.getInt("id"));
                    dish.setName(rsDish.getString("name"));
                    dish.setDishType(DishTypeEnum.valueOf(rsDish.getString("dish_type")));

                    // Chargement de la liste des ingrédients
                    psIng.setInt(1, id);
                    try (ResultSet rsIng = psIng.executeQuery()) {
                        List<Ingredient> ingredients = new ArrayList<>();
                        while (rsIng.next()) {
                            ingredients.add(mapResultSetToIngredient(rsIng));
                        }
                        dish.setIngredient(ingredients);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dish;
    }

    /**
     * b) Récupère les ingrédients avec pagination.
     */
    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredient LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToIngredient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    /**
     * c) Création d'ingrédients avec transaction et vérification de doublons.
     */
    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
        String insertSql = "INSERT INTO ingredient (name, price, category, id_dish) VALUES (?, ?, ?::category_enum, ?)";

        try (Connection conn = dbConnection.getDBConnection()) {
            conn.setAutoCommit(false); // Gestion manuelle de la transaction

            try (PreparedStatement psCheck = conn.prepareStatement(checkSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

                for (Ingredient ing : newIngredients) {
                    // 1. Vérification de l'existence
                    psCheck.setString(1, ing.getName());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            conn.rollback(); // Annulation totale
                            throw new RuntimeException("Erreur : l'ingrédient '" + ing.getName() + "' existe déjà.");
                        }
                    }

                    // 2. Insertion si inexistant
                    psInsert.setString(1, ing.getName());
                    psInsert.setDouble(2, ing.getPrice());
                    psInsert.setString(3, ing.getCategory().name());

                    if (ing.getDish() != null) {
                        psInsert.setInt(4, ing.getDish().getId());
                    } else {
                        psInsert.setNull(4, Types.INTEGER);
                    }
                    psInsert.executeUpdate();
                }
                conn.commit(); // Validation finale
            } catch (Exception e) {
                conn.rollback();
                throw (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newIngredients;
    }

    /**
     * Méthode utilitaire interne pour mapper les données.
     */
    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
        Ingredient ing = new Ingredient();
        ing.setId(rs.getInt("id"));
        ing.setName(rs.getString("name"));
        ing.setPrice(rs.getDouble("price"));
        // Utilise l'enum CategoryEnum définie dans les instructions
        ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
        return ing;
    }
}