//This app test bug:
//https://bugs.openjdk.java.net/browse/JDK-8028111
//hvivani-2014-10-04

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;

public class TestBug8028111 {

    public static void main(String[] args) throws Exception {

        String xml = "<?xml version=\"1.0\"?><test></test>";

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (int i = 0; i < 640000; i++) {

            ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
            factory.createXMLEventReader(stream);
            System.out.println("New stream: " + Integer.toString(i));
        }
    }
}
