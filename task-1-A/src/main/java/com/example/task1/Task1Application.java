package com.example.task1;



import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SpringBootApplication
public class Task1Application implements CommandLineRunner {

	@Autowired
		private BookRepository bookRepository;


		public static void main(String[] args) {
			SpringApplication.run(Task1Application.class, args);
		}

		@Override

		public void run(String... args) throws Exception {
			boolean dataImportedSuccessfully = false;
			try {
				// Load the XML file from the classpath
				Resource resource = new ClassPathResource("bgitook.xml");
				InputStream inputStream = resource.getInputStream();

				// Parse the XML document
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputStream);
				doc.getDocumentElement().normalize();

				// Extract data from XML and insert into the database
				NodeList nodeList = doc.getElementsByTagName("book");
				for (int temp = 0; temp < nodeList.getLength(); temp++) {
					Element element = (Element) nodeList.item(temp);
					String title = element.getElementsByTagName("title").item(0).getTextContent();
					String author = element.getElementsByTagName("author").item(0).getTextContent();
					int publishedYear = Integer.parseInt(element.getElementsByTagName("published_year").item(0).getTextContent());
					double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());

					// Check if the book already exists in the database


						Book book = new Book();
						book.setTitle(title);
						book.setAuthor(author);
						book.setPublished_year(publishedYear);
						book.setPrice(price);

						bookRepository.save(book);
						dataImportedSuccessfully = true;

				}
				if (dataImportedSuccessfully) {
					System.out.println("");
					System.out.println(" ");

					System.out.println("Data imported successfully.");
				} else {
					System.out.println("No new data imported. All books already exist in the database.");

					System.out.println("");
					System.out.println(" ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}




