package yorm.org.parse;

import yorm.org.entity.Product;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PromoWeekPage {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("_data");

    private final String baseUrl;

    public PromoWeekPage(String baseUrl) {
        this.baseUrl = baseUrl + resourceBundle.getString("promo_week_url");
    }

    public List<Product> getPromoProducts(){
        return null;
    }
}
