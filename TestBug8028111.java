//This app test bug:
//https://bugs.openjdk.java.net/browse/JDK-8028111
//hvivani-2014-10-04
//This will raise an exception when reaching 64000 expansions if the Java version does not support it.

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;

public class TestBug8028111 {

    public static void main(String[] args) throws Exception {

        String xml = "<?xml version=\"1.0\"?><test></test>";

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (int i = 0; i < 64000; i++) {

            ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
            factory.createXMLEventReader(stream);
            System.out.println("New stream: " + Integer.toString(i));
        }
    }
}
