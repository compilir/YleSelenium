import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class YleSeleniumSecond {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Miko ja Nastia\\Downloads\\gecko\\geckodriver.exe");

        Connection connection2 = PostgreSQLJDBC.connectionToDb();

        ArrayList<String> ehdokkaidenUrl = PostgreSQLJDBC.getEhdokkaat(connection2);

        connection2.close();

        //  ProfilesIni profileIni = new ProfilesIni();
        //  FirefoxProfile profile = profileIni.getProfile("proxy");
        //  options.setProfile(profile);

        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver1 = new FirefoxDriver(options);

        for (int y = 0; y < ehdokkaidenUrl.size(); y++) {
            System.out.println("Jyy on " + y);
            try {


                String url = ehdokkaidenUrl.get(y);
                driver1.get(url);
                Thread.sleep(2000);
                try {
                    driver1.findElement(By.className("ycd-consent-buttons__accept-all")).click();
                } catch (Exception hyvaksymiscatch) {
                }

                String puolue = driver1.findElement(By.className("CandidateIntro__PartyName-sc-11vufbf-4")).getText();

                List<WebElement> webElementList = driver1.findElements(By.className("Card-sc-g5jr5b-1"));

                for (int i = 0; i < webElementList.size(); i++) {
                    try {
                        WebElement webElement = webElementList.get(i).findElement(By.className("QuestionText__QuestionTextElement-sc-ayxp9a-0"));
                        String kysymys = webElement.getText();

                        Connection connection = PostgreSQLJDBC.connectionToDb();
                        PostgreSQLJDBC.addKysymys(connection, kysymys);
                        connection.close();

                        String henkKohtVastaus = "";

                        try {
                            henkKohtVastaus = webElementList.get(i).findElement(By.className("AnswersCard__Explabox-sc-1gm55j5-1")).getText();

                        } catch (Exception d) {
                        }

                        List<WebElement> kysymysRadioInput = webElementList.get(i).findElements(By.className("answerrow__StyledAnswerRow-sc-149c02d-0"));

                        WebElement vastaukset = kysymysRadioInput.get(1);

                        List<WebElement> nappulat = vastaukset.findElements(By.className("AnswerOption-sc-1yn435l-2"));

                        int moneskoNappula = 0;

                        for (int g = 0; g < nappulat.size(); g++) {
                            WebElement nappulaSis = nappulat.get(g).findElement(By.className("RoundOptionIndicator__AnswerOption-sc-qlgbuq-0"));

                            try {
                                nappulaSis.findElement(By.className("gZueme"));
                            } catch (Exception ei) {
                                moneskoNappula = g;
                            }
                        }

                        Connection connection1 = PostgreSQLJDBC.connectionToDb();
                        PostgreSQLJDBC.ehdokkaanvastaus(connection1, kysymys, henkKohtVastaus, moneskoNappula, url, puolue);
                        connection1.close();

                    } catch (Exception e) {
                    }
                }
            } catch (Exception sivunlataus) {
                System.out.println(sivunlataus);
            }
        }
    }
}






