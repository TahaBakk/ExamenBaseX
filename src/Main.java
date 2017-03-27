import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by x3727349s on 27/03/17.
 */
public class Main {


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String rutaXML = "/home/x3727349s/Música/examenBaseX/UF3-ExamenF-Plantes.xml";//cambiar por la ruta absoluta
        String bbdd = "tahabakk";//Nombre de la bbdd
        String username = "admin";//usuario de baseX
        String pass = "admin";//Contrasenya de baseX
        String consulta = "";//Consulta se añade en el switch dependiendo de la opcion seleccionada
        String ip ="localhost";//ip del server/baseX

        Scanner sc = new Scanner (System.in);

        BaseXConec basexconec = new BaseXConec();

        int numOpcion = 0;
        do {
            System.out.println("");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("1.Feu una connexió per a realitzar consultes al recurs. Indiqueu el nom de la planta de la que tenim més estoc. ");
            System.out.println("2.Quantes plantes en total hi ha a l'estoc?");
            System.out.println("3.Ens interessa saber el preu de tot l'estoc, planta per planta.");
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("");

            numOpcion = sc.nextInt();

            switch (numOpcion) {
                case 1:
                    consulta = "//PLANT[AVAILABILITY = max(//PLANT/AVAILABILITY)]/COMMON";//introducimos la consulta a realizar
                    System.out.println("Els països que conté son els següents");
                    basexconec.consulta(username, pass, rutaXML, bbdd, consulta, ip, "consulta");//llamamos el metodo pasandole los datos cuando le paso lo de tipo es solo para saber si llamar al otro metodo o no
                    break;

                case 2:
                    consulta = "/";
                    basexconec.consulta(username, pass, rutaXML, bbdd, consulta, ip, "segunda");
                    break;

                case 3:
                    consulta = "/";
                    System.out.println("Les dades del preu de l'estoc és de: ");
                    basexconec.consulta(username, pass, rutaXML, bbdd, consulta, ip, "parserXML");

                    break;

                case 4:
                    basexconec.crearRecurso(username,pass,ip );
                    break;

                default:
                    break;
            }

        }while (numOpcion!=0);//si es 0 sale del blucle y finaliza


        System.out.println("Programa finalizado ^^");

    }
}



