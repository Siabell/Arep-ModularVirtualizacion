package edu.escuelaing.arep;

/**
 * Maneja el servicio del servidor
 * @author vales
 *
 */
public class Manager {

	public static void main(String[] args)  {
		Server.iniciar();
		Server.listen();
	}
}
