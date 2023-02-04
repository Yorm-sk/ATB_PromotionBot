package yorm.org.parse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import yorm.org.entity.Product;
import yorm.org.excepions.CanNotWriteToFileException;
import yorm.org.excepions.ConnectionToUrlException;
import yorm.org.excepions.FileNotFoundException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PromoWeekPage {

    private final static Logger LOGGER = LogManager.getLogger(PromoWeekPage.class);

    private final String url;

    private Date currentDate;

    private Date endOfPromotion;

    private final Path pathToDate = Paths.get(System.getProperty("user.dir"), "resources", "date.dat");

    private final Path pathToProduct = Paths.get(System.getProperty("user.dir"), "resources", "products.dat");

    private final Document document;

    public PromoWeekPage() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("_data");
        ResourceBundle resourceBundle1 = ResourceBundle.getBundle("_config");
        url = resourceBundle1.getString("base_url") + resourceBundle.getString("promo_week_url");
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            LOGGER.error("Can`t connect to " + url);
            throw new ConnectionToUrlException(url);
        }
        endOfPromotion = getEndOfPromotion();
        currentDate = new Date();
    }

    public List<Product> getPromoProducts() {
        currentDate = new Date();
        if (currentDate.after(endOfPromotion)) {
            endOfPromotion = updatePromotion();
            return updateProducts();
        } else return getProducts();
    }

    private List<Product> updateProducts() {
        List<Product> products = new ArrayList<>();
        int pageQuantity = document.getElementsByClass("product-pagination__item").size() - 2;
        for (int i = 1; i <= pageQuantity; i++) {
            try {
                Document documentOnPage = Jsoup.connect(url + "?page=" + i).get();
                Elements productElement = documentOnPage.getElementsByClass("catalog-item");
                for (Element product : productElement) {
                    if (!product.getElementsByClass("custom-product-label").isEmpty()) {
                        String name = product.getElementsByClass("catalog-item__title").get(0).text();
                        double storePrice = Double.parseDouble(product.getElementsByClass("product-price__bottom").get(0).text());
                        double salesPrice = Double.parseDouble(product.getElementsByClass("product-price__top").
                                get(0).child(0).text());
                        String img = product.getElementsByClass("catalog-item__img").get(0).attr("src");
                        Product parsedProduct = new Product(name, storePrice, salesPrice, img);
                        products.add(parsedProduct);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Can`t connect to " + url);
                throw new ConnectionToUrlException(url);
            }
        }
        try {
            writeProductsToFile(products);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new CanNotWriteToFileException(e.getMessage());
        }
        return products;
    }

    private List<Product> getProducts() {
        File file = new File(pathToProduct.toString());
        if (file.exists()) {
            Object result;
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                result = ois.readObject();
            } catch (FileNotFoundException e) {
                LOGGER.error("File by path " + pathToDate + " was not found");
                throw new FileNotFoundException(pathToDate.toString());
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            return (List<Product>) result;
        } else {
            return updateProducts();
        }
    }

    private Date updatePromotion() {
        Elements timer = document.getElementsByClass("actionsTimer");
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy k:mm:ss", Locale.ENGLISH);
        Date date;
        try {
            date = formatter.parse(timer.get(0).attr("data-time"));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            writeDateToFile(date);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new CanNotWriteToFileException(e.getMessage());
        }
        return date;
    }

    private Date getEndOfPromotion() {
        File file = new File(pathToDate.toString());
        if (file.exists()) {
            Object result;
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                result = ois.readObject();
            } catch (FileNotFoundException e) {
                LOGGER.error("File by path " + pathToDate + " was not found");
                throw new FileNotFoundException(pathToDate.toString());
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            return (Date) result;
        } else {
            return updatePromotion();
        }
    }

    private void writeDateToFile(Date date) throws IOException {
        FileOutputStream fos = new FileOutputStream(pathToDate.toString());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(date);
        oos.close();
    }

    private void writeProductsToFile(List<Product> products) throws IOException {
        FileOutputStream fos = new FileOutputStream(pathToProduct.toString());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(products);
        oos.close();
    }
}
