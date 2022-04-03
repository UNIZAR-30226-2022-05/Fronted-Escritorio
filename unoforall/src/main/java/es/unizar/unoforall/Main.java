package es.unizar.unoforall;

import java.util.concurrent.ExecutionException;

public class Main {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/*
		 * Para que no de error de Error: JavaFX runtime components are missing, and are required to run this application
		 * 	al ejecutar el .jar generado, hay que crear una clase "Main falsa" que ejecute el método main
		 *  de la aplicación javafx. En todos los sitios donde haya que introducir la clase principal del POM.xml,
		 *  introducir el Main falso
		 */
		
		App.main(args);
	}
}
