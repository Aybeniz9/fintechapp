package az.edu.turing.service.currency;

import az.edu.turing.model.dto.currency.CurrencyDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

@Service
public class CurrencyService {
    @SneakyThrows
    private Document configCurrency() {
        URL url = new URL("https://www.cbar.az/currencies/19.09.2025.xml");
        InputStream inputStream = url.openStream();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        return doc;
    }


    public CurrencyDto usd() {
        Document document = configCurrency();

        NodeList valuteList = document.getElementsByTagName("Valute");
        CurrencyDto currencyDto = new CurrencyDto();

        for (int i = 0; i < valuteList.getLength(); i++) {
            Element valute = (Element) valuteList.item(i);
            String code = valute.getAttribute("Code");
            if (code.equals("USD")) {
                String value = valute.getElementsByTagName("Value").item(0).getTextContent();
                System.out.println("USD Rate: " + value);
                currencyDto.setValue(value);
            }
        }
        return currencyDto;
    }

    public CurrencyDto euro() {
        Document document = configCurrency();
        NodeList valuteList = document.getElementsByTagName("Valute");
        CurrencyDto currencyDto = new CurrencyDto();
        for (int i = 0; i < valuteList.getLength(); i++) {
            Element valute = (Element) valuteList.item(i);
            String code = valute.getAttribute("Code");
            if (code.equals("EUR")) {
                String value = valute.getElementsByTagName("Value").item(0).getTextContent();
            currencyDto.setValue(value);
            }
        }
        return currencyDto;
    }
    public CurrencyDto trl(){
        Document document = configCurrency();
        NodeList valuteList = document.getElementsByTagName("Valute");
        CurrencyDto currencyDto = new CurrencyDto();
        for (int i = 0; i < valuteList.getLength(); i++) {
            Element valute = (Element) valuteList.item(i);
            String code = valute.getAttribute("Code");
            if (code.equals("TRY")) {
                String value = valute.getElementsByTagName("Value").item(0).getTextContent();
            }
        }
        return currencyDto;
    }

}
