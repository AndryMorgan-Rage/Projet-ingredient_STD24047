import java.time.Instant;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();
        Order ord = dataRetriever.findOrderByReference("ORD 102");
        ord.setType(OrderTypeEnum.valueOf("TAKE_AWAY"));
        ord.setStatus(OrderStatusEnum.DELIVERED);
        dataRetriever.saveOrder(ord);

    }
}



