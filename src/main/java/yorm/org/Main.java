package yorm.org;

import yorm.org.entity.Product;
import yorm.org.parse.PromoWeekPage;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PromoWeekPage promoWeekPage = new PromoWeekPage();
        List<Product> products = promoWeekPage.getPromoProducts();
        System.out.println(products);
    }
}
