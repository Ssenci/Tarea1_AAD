package tarea1.vista;

import java.io.IOException;
import java.util.Scanner;

import tarea1.datos.LeerDAT;
import tarea1.datos.LeerXml;
import tarea1.logica.Peregrino;
import tarea1.utils.ComprobacionArgumentos;

public class Menu {

	private Scanner teclado;

	public Menu() {
		teclado = new Scanner(System.in);
	}

	public void run() {
		System.out.println("1-Registrarse como peregrino 2-Login 3-Salir");
		int res = teclado.nextInt();
		teclado.nextLine(); // limpieza de buffer

		while (res != 3) {
			if (res == 1) {
				this.registrar();
			}
			if (res == 2) {
				this.login();
			}

			res = teclado.nextInt();
		}
	}

	public void registrar() {

		boolean aux = false;
		String usuario = " ";
		String contraseña = " ";
		String nacionalidad = " ";

		do {
			try {
				System.out.print("Nombre de usuario: ");

				usuario = teclado.nextLine();

				ComprobacionArgumentos.esNulo(usuario);
				ComprobacionArgumentos.esVacio(usuario);
				ComprobacionArgumentos.esInvalido(usuario);

				aux = false;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		do {
			try {
				System.out.print("Contraseña: ");

				contraseña = teclado.nextLine();

				ComprobacionArgumentos.esNulo(contraseña);
				ComprobacionArgumentos.esVacio(contraseña);
				ComprobacionArgumentos.esInvalido(contraseña);
				ComprobacionArgumentos.comprueba(contraseña.length() > 16, "contraseña demasiado larga");

				aux = false;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		// añadir al peregrino nacionalidad

		do {
			try {
				System.out.print("Nacionalidad: ");

				nacionalidad = teclado.nextLine();

				ComprobacionArgumentos.esNulo(nacionalidad);
				ComprobacionArgumentos.esVacio(nacionalidad);
				ComprobacionArgumentos.esInvalido(nacionalidad);
				ComprobacionArgumentos.comprueba(!LeerXml.obtenerPaises().contains(nacionalidad),
						"La nacionalidad no concuerda con ninguna nacionalidad de la lista");

				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		// asociar parada a peregrino(pregunta al usuario la parada (id) y asociarsela)
		try {
			LeerDAT.obtenerParadas();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		;
		// asociar contraseña a peregrino
	}

	public void login() {

	}

	public void exportar() {

	}

}
