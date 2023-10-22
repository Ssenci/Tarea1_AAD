package tarea1.vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tarea1.datos.CrearCredenciales;
import tarea1.datos.LeerCredenciales;
import tarea1.datos.LeerDAT;
import tarea1.datos.LeerXml;
import tarea1.logica.Parada;
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
				this.login();
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
		int idParadaActual = 0;
		String respuesta = " ";
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		ArrayList<Peregrino> listaPeregrinos = new ArrayList<Peregrino>();
		ArrayList<Peregrino> listaCredenciales = new ArrayList<Peregrino>();
		long idMayor = Long.MIN_VALUE;
		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		do {
			try {
				System.out.print("Nombre de usuario: ");

				usuario = teclado.nextLine();

				ComprobacionArgumentos.esNulo(usuario);
				ComprobacionArgumentos.esVacio(usuario);
				ComprobacionArgumentos.esInvalido(usuario);

				System.out.print("Contraseña: ");

				contraseña = teclado.nextLine();

				ComprobacionArgumentos.esNulo(contraseña);
				ComprobacionArgumentos.esVacio(contraseña);
				ComprobacionArgumentos.esInvalido(contraseña);
				ComprobacionArgumentos.comprueba(contraseña.length() > 16, "contraseña demasiado larga");

				System.out.print("Nacionalidad: ");

				nacionalidad = teclado.nextLine();

				ComprobacionArgumentos.esNulo(nacionalidad);
				ComprobacionArgumentos.esVacio(nacionalidad);
				ComprobacionArgumentos.esInvalido(nacionalidad);
				ComprobacionArgumentos.comprueba(!LeerXml.obtenerPaises().contains(nacionalidad),
						"La nacionalidad no concuerda con ninguna nacionalidad de la lista");
				// asociar parada a peregrino(pregunta al usuario la parada (id) y asociarsela)
				System.out.print("Dime la id de la parada desde la que te registras: ");

				idParadaActual = teclado.nextInt();

				// Comprabar que no sea null(validacion) "en este caso no hace falta validar"
				// Comprobar que idparadaactual este dentro de las id´s de paradas.dat
				// (verificacion)

				ComprobacionArgumentos.comprueba(!listaParadas.contains(new Parada(idParadaActual)),
						"Error: la id que has pasado, no existe");

				System.out.print("Los datos introducidos son correctos? (S/N)");
				teclado.nextLine();
				respuesta = teclado.nextLine();

				ComprobacionArgumentos.esNulo(respuesta);
				ComprobacionArgumentos.esVacio(respuesta);
				ComprobacionArgumentos.esInvalido(respuesta);
				ComprobacionArgumentos.comprueba(respuesta.length() > 1,
						"Error: respuesta damasiado larga, usa S para si y N para no");
				ComprobacionArgumentos.comprueba(!respuesta.equals("S") && !respuesta.equals("s"),
						"Vuelve a introducir todos los datos de nuevo");

				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		// Comparar los datos asociados al nuevo peregrino con el fichero
		// credenciales.txt
		// en caso de existir syso:Error. usuario ya existente. en caso de no existir
		// añadir al txt y logearlo automaticamente

		System.out.println("Comparando campos con ficheros...");
		try {
			listaCredenciales = LeerCredenciales.obtenerPeregrino();
			ComprobacionArgumentos.comprueba(listaCredenciales.contains(new Peregrino(usuario)),
					"Error: Usuario en uso, repite el proceso utilizando otro nombre distinto");

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		System.out.println("Campos correctos y libres, iniciando sesion...");

		// asociar campos a peregrino
		for (Peregrino a : listaCredenciales) {
			if (a.getId() > idMayor)
				idMayor = a.getId();

		}
		try {
			CrearCredenciales.añadirCredenciales(usuario, contraseña, "peregrino", idMayor + 1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void login() {
		boolean aux = false;
		ArrayList<Peregrino> listaCredenciales = new ArrayList<Peregrino>();
		System.out.println("Vamos a logearnos");
		try {
			listaCredenciales = LeerCredenciales.obtenerPeregrino();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		do {
			try {
				System.out.print("Nombre de usuario: ");
				String usuario = teclado.nextLine();
				System.out.println("Contraseña: ");
				String contraseña = teclado.nextLine();

				if (listaCredenciales.contains(usuario) && listaCredenciales.contains(contraseña)) {
					System.out.println("Credenciales correctas, bienvenido...");
				} else
					System.out.println("Credenciales incorrectas, intentelo de nuevo");

				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		System.out.println("1-Registrar nueva parada(ADMIN) 2-Exportar carnet XML(PEREGRINO) 3-Logout");

	}

	public void exportar() {

	}

}
