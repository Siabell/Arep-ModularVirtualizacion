package edu.escuelaing.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.escuelaing.arep.annotations.Web;

import java.io.*;
import java.util.*;
import java.net.*;


public class Server {

	private static Map<String,Method> webMethods = new HashMap();
	private static int port = getPort();
	private static ServerSocket serverSocket;
	private static ExecutorService executor;
	private final static int nThreads = 15;
	
	/**
	 * Inicia el servicio del server, cargando todas las clases que posean
	 * la annotacion web y las agrega a un hashmap donde estan los metodos con
	 * esta anotacion
	 */
	public static void iniciar() {
		String pathP = "edu.escuelaing.arep.webservice";
		String path = "edu/escuelaing/arep/webservice";
		System.out.println(path);
		
		ArrayList<File> dirExist = new ArrayList<File>();
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			
			Enumeration<URL> resources = cld.getResources(path);
			System.out.println(resources.getClass().toString());
			while (resources.hasMoreElements()) {
				dirExist.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
				System.out.println("1");
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dirExist.size());
		addMethodswithWeb(path + "." );
		for (File directory : dirExist) {
			 if (directory.exists()) {
				 for (String file : directory.list()) {
	                    if (file.endsWith(".class")) {
	                        String fileClass = file.substring(0, file.indexOf("."));
	                        System.out.println(pathP + "." + fileClass + "      ------------");
	                        addMethodswithWeb(pathP + "." + fileClass);
	                        
	                    }
	                }
			 }
		}
	}
	
	
	/**
	 * Escucha el puerto y atiende los request de los clientes diferenciendo entre peticiones
	 * estaticas(serverhttp) y dinamicas(annotacion web)
	 */
	public static void listen()  {
		
		try { 
		      serverSocket = new ServerSocket(port);
		      executor = Executors.newFixedThreadPool(nThreads);
		   } catch (IOException e) {
		      System.err.println("Could not listen on port: " + port);
		   }
		
		while (true) {
			
			System.out.println("Listo para recibir ...");
			   //clientSocket = serverSocket.accept();
			   //System.out.println(clientSocket);
			   HttpServer thread = (new HttpServer(serverSocket,webMethods));
			   thread.run();
			   //executor.execute(new HttpServer(serverSocket,webMethods));
			
			
		}
		
	}
	
	/**
	 * Agrega a el hashmap de metodos con la annotacion web un nuevo metodo que tenga esta anotacion
	 * @param className nombre de la clase a probar si tiene la anotacion
	 */
	private static void addMethodswithWeb(String className) {
		try {
			Class c = Class.forName(className);
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if(m.isAnnotationPresent(Web.class)) {
					System.out.println("Cargando metodos con annotacion web");
					webMethods.put("/"+m.getAnnotation(Web.class).value(),m);
					System.out.println(m.getAnnotation(Web.class).value());
				}
			}
		}catch (ClassNotFoundException ex) {			
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * Retorna un string con la invocacion de un metodo
	 * @param m metodo a invocar
	 * @return Strinf de la invocacion del metodo
	 */
	private static String invokeMethods(Method m) {
		String answer = null;
		try {
			answer = (String) m.invoke(null, null);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}
	
	/**
     * Retorna el puerto por el que va a escuchar el servidor
     * @return puerto por el que va a escuchar el servidor
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
