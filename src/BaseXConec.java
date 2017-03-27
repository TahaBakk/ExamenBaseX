import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by x3727349s on 27/03/17.
 */
public class BaseXConec {


    public static void consulta(String username, String pass, String ruta, String bbdd, String consulta, String ip, String tipo){

        try (ClientSession session = new ClientSession(ip, 1984, username, pass)) {//IP, PUERTO, USERNAME, PASSWORD
            session.execute(new CreateDB(bbdd, ruta));//BBDD que se creara, y ruta del fitxero
            try (ClientQuery query = session.query(consulta)) {//consulta
                if (tipo == "consulta"){
                System.out.println(query.execute());//imprimir resultado
                }else if (tipo.equalsIgnoreCase("parserXML")){
                    parserXML(query.execute(),"tercera");
                }else if (tipo.equalsIgnoreCase("segunda")){
                    parserXML(query.execute(),"segunda");
                }
            } catch (SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
            session.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parserXML (String consulta, String tipo) throws ParserConfigurationException, IOException, SAXException {

        int estocMaximo = 0;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(new InputSource(new StringReader(consulta)));
        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getChildNodes();
        if (nl != null) {
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    if (el.getNodeName().contains("PLANT")) {
                        String common = el.getElementsByTagName("COMMON").item(0).getTextContent();
                        String price = el.getElementsByTagName("PRICE").item(0).getTextContent();
                        String availability = el.getElementsByTagName("AVAILABILITY").item(0).getTextContent();
                        String nan = price.substring(1);
                        double preu = Double.parseDouble(nan) * Double.parseDouble(availability);
                        if(tipo.equalsIgnoreCase("tercera")) {
                            System.out.println("De la planta " + common + " tenim " + availability + " exemplars. Cada exemplar té un preu de " + price + "per tant per la venta de tot l'stock ingresarem " + preu + " dolars");
                        }else if (tipo.equalsIgnoreCase("segunda")){
                            estocMaximo += Integer.parseInt(el.getElementsByTagName("AVAILABILITY").item(0).getTextContent());
                        }
                    }
                }
            }
        }

        if(tipo.equalsIgnoreCase("segunda")){
            System.out.println("Al estoc hi ha un total de "+estocMaximo+" de plantes");
        }

    }


    public static void crearRecurso(String username, String pass, String ip) throws IOException {
        BaseXClient session = new BaseXClient(ip, 1984, username, pass);
        session.execute("create db TahaBakk");
        System.out.println(session.info());
        ByteArrayInputStream bais = new ByteArrayInputStream("<x>Hello World!</x>".getBytes());
        session.add("TahaBakk/UF3-ExamenF-Plantes.xml", bais);
        System.out.println(session.info());

    }

}


