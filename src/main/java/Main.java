import java.time.Instant;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
      //  DataRetriever dataRetriever = new DataRetriever();
       // Dish saladeVerte = dataRetriever.findDishById(1);
       // System.out.println(saladeVerte);

      //  Dish poulet = dataRetriever.findDishById(2);
      //  System.out.println(poulet);

       // Dish rizLegume = dataRetriever.findDishById(3);
    //    rizLegume.setPrice(100.0);
    //    Dish newRizLegume = dataRetriever.saveDish(rizLegume);
    //    System.out.println(newRizLegume); // Should not throw exception


//        Dish rizLegumeAgain = dataRetriever.findDishById(3);
//        rizLegumeAgain.setPrice(null);
//        Dish savedNewRizLegume = dataRetriever.saveDish(rizLegume);
//        System.out.println(savedNewRizLegume); // Should throw exception

    //    Ingredient laitue = dataRetriever.findIngredientById(1);
      //  System.out.println(laitue);


                DataRetriever retriever = new DataRetriever();

                // 1. Création d'une nouvelle commande (CREATED / EAT_IN)
                Order newOrder = new Order();
                newOrder.setReference("CMD-TEST-001");
                newOrder.setCreationDatetime(Instant.now());
                newOrder.setType(OrderTypeEnum.EAT_IN);
                newOrder.setStatus(OrderStatusEnum.CREATED);
                newOrder.setDishOrderList(new ArrayList<>()); // Ajoute des plats si nécessaire

                System.out.println("--- Test 1 : Sauvegarde initiale ---");
                retriever.saveOrder(newOrder);
                System.out.println("Commande sauvegardée avec succès !");

                // 2. Test du findOrderByReference
                System.out.println("\n--- Test 2 : Récupération ---");
                Order found = retriever.findOrderByReference("CMD-TEST-001");
                System.out.println("Type récupéré : " + found.getType());
                System.out.println("Statut récupéré : " + found.getStatus());

                // 3. Mise à jour en DELIVERED
                System.out.println("\n--- Test 3 : Passage en DELIVERED ---");
                found.setStatus(OrderStatusEnum.DELIVERED);
                retriever.saveOrder(found);
                System.out.println("Commande marquée comme LIVRÉE.");

                // 4. Tentative de modification interdite (Question 2 du sujet)
                System.out.println("\n--- Test 4 : Tentative de modification d'une commande livrée ---");
                try {
                    found.setType(OrderTypeEnum.TAKE_AWAY); // On essaie de changer le type
                    retriever.saveOrder(found);
                    System.out.println("ERREUR : La modification aurait dû être bloquée !");
                } catch (RuntimeException e) {
                    System.out.println("SUCCÈS : L'exception a bien été levée : " + e.getMessage());
                }
            }
        }



