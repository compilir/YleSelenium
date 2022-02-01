import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class YleSeleniumMain {
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {
        System.setProperty("webdriver.gecko.driver", "\\gecko\\geckodriver.exe");

        ProfilesIni profileIni = new ProfilesIni();
      //  FirefoxProfile profile = profileIni.getProfile("proxy");
        FirefoxOptions options = new FirefoxOptions();
      //  options.setProfile(profile);

        WebDriver driver1 = new FirefoxDriver(options);

        for (int kierros = 0; kierros < 22; kierros ++) {
            ArrayList<String> listOfCandidates = new ArrayList<>();

        String url = "https://vaalikone.yle.fi/aluevaalit2022/" + kierros +"/ehdokkaat/";

        driver1.get(url);

        Thread.sleep(2000);

        try {
            driver1.findElement(By.className("ycd-consent-buttons__accept-all")).click();
        }catch (Exception hyvaksymiscatch) {
        }
        JavascriptExecutor js = (JavascriptExecutor) driver1;
        for (int i = 0; i < 200; i ++) {
            js.executeScript("window.scrollBy(0,400)", "");
            List<WebElement> webElementList = driver1.findElements(By.className("CandidateCard__BottomRow-sc-3boxem-2"));
            for (int x = 0; x < webElementList.size(); x ++) {
                String nimi = webElementList.get(x).findElement(By.className("ItemLink__ItemLinkContainer-sc-1j8chk7-0")).findElement(By.tagName("a")).getAttribute("href");
                Boolean sisaltyy = false;
                for (String listOfCandidate : listOfCandidates) {
                    if (listOfCandidate.contains(nimi)) {
                        sisaltyy = true;
                    }
                }
                if (!sisaltyy) {
                    listOfCandidates.add(nimi);
                }
            }
        }

        for (int y = 0; y < listOfCandidates.size(); y ++) {
            System.out.println(y);
            Connection connection = PostgreSQLJDBC.connectionToDb();
            PostgreSQLJDBC.addEhdokas(connection, listOfCandidates.get(y));
            connection.close();
        }
    }


    }
}
