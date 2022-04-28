package es.unizar.unoforall;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

public class ConfCuentaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	private static HashMap<Integer,Image> fondos = new HashMap<Integer, Image>();
	static {
		fondos.put(0, new Image(App.class.getResourceAsStream("images/fondos/azul.png")));
		fondos.put(1, new Image(App.class.getResourceAsStream("images/fondos/morado.png")));
		fondos.put(2, new Image(App.class.getResourceAsStream("images/fondos/gris.png")));
	}
	
	@FXML private VBox fondo;
	
	@FXML private ImageView imgMenu;
	
	@FXML private Button btnEliminar;
	
	@FXML private Label labelError;
	@FXML TextField cajaNombre;	
	@FXML TextField cajaCorreo;	
	@FXML PasswordField cajaContrasenya;
	@FXML PasswordField cajaContrasenya2;
	
	@FXML VBox contenedorOculto;
	@FXML TextField cajaCodigo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(
			new Background(
				new BackgroundImage(
						fondos.get(App.getPersonalizacion().get("tableroSelec")),
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						BackgroundSize.DEFAULT
					)
				)
			);
		
		//CONFIGURACION DE EFECTO DE HOVER
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});;
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});;
		

		btnEliminar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnEliminar.setEffect(new Glow(0.5));
			}
		});;
		btnEliminar.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnEliminar.setEffect(null);
			}
		});;
	}
	
	@FXML
	public void autoCompletar(ActionEvent event) {	
		RestAPI apirest = new RestAPI("/api/sacarUsuarioVO");
		String sesionID = App.getSessionID();
		apirest.addParameter("sesionID",sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	UsuarioVO retorno = apirest.receiveObject(UsuarioVO.class);
    	if (retorno.isExito()) {
    		cajaNombre.setText(StringUtils.parseString(retorno.getNombre()));
    		cajaCorreo.setText(StringUtils.parseString(retorno.getCorreo()));
    	} else {
    		labelError.setText("No se han podido autocompletar los datos de la cuenta");
    	}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		labelError.setText("");
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = new RestAPI("/api/actualizarCancel");
			String sesionID = App.getSessionID();
			apirest.addParameter("sesionID",sesionID);
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno != null) {
	    		labelError.setText(StringUtils.parseString(retorno));
	    		if (DEBUG) System.out.println(StringUtils.parseString(retorno));
	    	} else {
				App.setRoot("principal");
			}
		} else {
			App.setRoot("principal");
		}
	}

	@FXML
    private void goToMain(Event event) {
		labelError.setText("");
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = new RestAPI("/api/actualizarCancel");
			String sesionID = App.getSessionID();
			apirest.addParameter("sesionID",sesionID);
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno != null) {
	    		labelError.setText(StringUtils.parseString(retorno));
	    		if (DEBUG) System.out.println(retorno);
	    	}
		}
    	App.setRoot("principal");
	}

	@FXML
	private void actualizarCuenta(ActionEvent event) {
		labelError.setText("");
		String nuevoCorreo = cajaCorreo.getText();
		String nuevoNombre = cajaNombre.getText();
		String nuevaContrasenna = cajaContrasenya.getText();
		String confirmarContrasenna = cajaContrasenya2.getText();
    	
		if (nuevoCorreo == null || nuevoNombre == null || nuevaContrasenna == null ) {
			labelError.setText("Faltan parámetros");
			if (DEBUG) System.out.println("Faltan parámetros");
		} else if (!nuevaContrasenna.equals(confirmarContrasenna)) {
			labelError.setText("Las contraseñas no coinciden");
			if (DEBUG) System.out.println("Las contraseñas no coinciden");
		} else {
			RestAPI apirest = new RestAPI("/api/actualizarCuentaStepOne");
			String sesionID = App.getSessionID();
			apirest.addParameter("sesionID",sesionID);
			apirest.addParameter("correoNuevo",nuevoCorreo);
			apirest.addParameter("nombre",nuevoNombre);
			apirest.addParameter("contrasenna",HashUtils.cifrarContrasenna(nuevaContrasenna));
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno == null) {
	    		desocultarContenedorOculto();
	    	} else {
	    		labelError.setText(StringUtils.parseString(retorno));
	    		if (DEBUG) System.out.println(retorno);
	    	}
		}
	}
	
	private void desocultarContenedorOculto() {
		contenedorOculto.setDisable(false);
		contenedorOculto.setVisible(true);
	}
	
	@FXML
	private void confirmarCodigo (ActionEvent event) {
		labelError.setText("");
		Integer codigo = 0;
		//Si el código no es un entero, no es válido
		try {
			codigo = Integer.parseInt(cajaCodigo.getText());
		} catch (Exception e) {
			labelError.setText("Por favor introduzca un código válido");
			return;
		}
		RestAPI apirest = new RestAPI("/api/actualizarCuentaStepTwo");
		String sesionID = App.getSessionID();
		apirest.addParameter("sesionID",sesionID);
		apirest.addParameter("codigo",codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
		String retorno = apirest.receiveObject(String.class);
    	if (retorno == null) {
    		if (DEBUG) System.out.println("Exito.");
    		//Para ocultar la caja del código una vez ya se ha usado
    		contenedorOculto.setDisable(true);
    		contenedorOculto.setVisible(false);
    	} else {
    		labelError.setText(StringUtils.parseString(retorno));
    		if (DEBUG) System.out.println(retorno);
    	}
	}
	
	@FXML
    private void deleteAccount(ActionEvent event) {
		RestAPI apirest = new RestAPI("/api/borrarCuenta");
		String sesionID = App.getSessionID();
		apirest.addParameter("sesionID",sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	String retorno = apirest.receiveObject(String.class);
    	if (retorno.equals("BORRADA")) {
	    	App.setRoot("login");
	        App.cerrarConexion();
	    	if (DEBUG) System.out.println("Cuenta eliminada");
    	} else {
    		labelError.setText(StringUtils.parseString(retorno));
    		if (DEBUG) System.out.println(retorno);
    	}
	}

}
