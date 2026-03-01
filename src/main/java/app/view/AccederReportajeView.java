package app.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;

public class AccederReportajeView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Componentes públicos para que el controlador los gestione
	public JTable tableEventos;
	public JTextField txtTitulo;
	public JTextField txtSubtitulo;
	public JTextArea txtCuerpo;
	public JLabel lblFechaVersion;
	public JLabel lblHoraVersion;
	public JLabel lblNombreEmpresa; 

	public AccederReportajeView() {
		setTitle("Visualización de Reportajes Autorizados");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- PARTE SUPERIOR: NOMBRE DE LA EMPRESA (CONTEXTO) ---
		lblNombreEmpresa = new JLabel("EMPRESA: ");
		lblNombreEmpresa.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNombreEmpresa.setBounds(20, 10, 800, 25);
		contentPane.add(lblNombreEmpresa);

		// --- PANEL IZQUIERDO: REPORTAJES AUTORIZADOS ---
		JLabel lblLista = new JLabel("REPORTAJES AUTORIZADOS");
		lblLista.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLista.setBounds(20, 50, 250, 14);
		contentPane.add(lblLista);

		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setBounds(20, 75, 300, 450);
		contentPane.add(scrollTable);
		
		tableEventos = new JTable();
		scrollTable.setViewportView(tableEventos);

		// --- PANEL DERECHO: CONTENIDO DEL REPORTAJE ---
		JLabel lblDetalle = new JLabel("CONTENIDO DEL REPORTAJE");
		lblDetalle.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDetalle.setBounds(350, 50, 300, 14);
		contentPane.add(lblDetalle);

		// Metadatos de la última versión (Anchos corregidos para evitar "...")
		lblFechaVersion = new JLabel("ÚLTIMA VER: --/--/--");
		lblFechaVersion.setBounds(630, 50, 150, 14); 
		contentPane.add(lblFechaVersion);

		lblHoraVersion = new JLabel("HORA: --:--");
		lblHoraVersion.setBounds(780, 50, 100, 14);
		contentPane.add(lblHoraVersion);

		JLabel lblTit = new JLabel("TÍTULO:");
		lblTit.setBounds(350, 80, 100, 14); 
		contentPane.add(lblTit);

		txtTitulo = new JTextField();
		txtTitulo.setEditable(false); // Solo lectura
		txtTitulo.setBounds(350, 100, 500, 25);
		contentPane.add(txtTitulo);

		JLabel lblSub = new JLabel("SUBTÍTULO:");
		lblSub.setBounds(350, 140, 100, 14); 
		contentPane.add(lblSub);

		txtSubtitulo = new JTextField();
		txtSubtitulo.setEditable(false); // Solo lectura
		txtSubtitulo.setBounds(350, 160, 500, 25);
		contentPane.add(txtSubtitulo);

		JLabel lblCuerpo = new JLabel("CUERPO:");
		lblCuerpo.setBounds(350, 200, 100, 14); 
		contentPane.add(lblCuerpo);

		JScrollPane scrollCuerpo = new JScrollPane();
		scrollCuerpo.setBounds(350, 220, 500, 305);
		contentPane.add(scrollCuerpo);

		txtCuerpo = new JTextArea();
		txtCuerpo.setEditable(false); // Solo lectura
		txtCuerpo.setLineWrap(true);
		txtCuerpo.setWrapStyleWord(true);
		scrollCuerpo.setViewportView(txtCuerpo);
	}
}