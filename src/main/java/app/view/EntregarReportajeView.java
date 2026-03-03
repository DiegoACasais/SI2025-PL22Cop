package app.view;

import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import app.dto.EntregarReportajeDTO;

public class EntregarReportajeView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldTitulo;
    private JTextField textFieldSubtitulo;
    private JTextArea textAreaCuerpo;
    private JComboBox<EntregarReportajeDTO> comboEventos;
    private JComboBox<String> comboFiltro;
    private JButton btnAceptar;
    private JButton btnRestaurarVersion;
    private JLabel lblPermisos;
    private JComboBox<String> comboVersiones;
    private JTextField textFieldSubtituloVersion;
    private JTextArea textAreaCuerpoVersion;

    public JComboBox<EntregarReportajeDTO> getComboEventos() {
        return comboEventos;
    }

    public JComboBox<String> getComboFiltro() {
        return comboFiltro;
    }

    public JComboBox<String> getComboVersiones() {
        return comboVersiones;
    }

    public int getIndiceVersionSeleccionada() {
        return comboVersiones.getSelectedIndex();
    }

    public String getTitulo() {
        return textFieldTitulo.getText();
    }

    public String getSubtitulo() {
        return textFieldSubtitulo.getText();
    }

    public String getCuerpo() {
        return textAreaCuerpo.getText();
    }

    public void setTitulo(String titulo) {
        textFieldTitulo.setText(titulo == null ? "" : titulo);
    }

    public void setSubtitulo(String subtitulo) {
        textFieldSubtitulo.setText(subtitulo == null ? "" : subtitulo);
    }

    public void setCuerpo(String cuerpo) {
        textAreaCuerpo.setText(cuerpo == null ? "" : cuerpo);
    }

    public void setSubtituloVersion(String subtitulo) {
        textFieldSubtituloVersion.setText(subtitulo == null ? "" : subtitulo);
    }

    public void setCuerpoVersion(String cuerpo) {
        textAreaCuerpoVersion.setText(cuerpo == null ? "" : cuerpo);
    }

    public void limpiarVersionSeleccionada() {
        setSubtituloVersion("");
        setCuerpoVersion("");
    }

    public void setTituloEditable(boolean editable) {
        textFieldTitulo.setEditable(editable);
    }

    public void setContenidoEditable(boolean editable) {
        textFieldSubtitulo.setEditable(editable);
        textAreaCuerpo.setEditable(editable);
    }

    public void setSeccionVersionesVisible(boolean visible) {
        comboVersiones.setEnabled(visible);
        textFieldSubtituloVersion.setEnabled(visible);
        textAreaCuerpoVersion.setEnabled(visible);
        btnRestaurarVersion.setEnabled(visible);
    }

    public void setTextoBotonAceptar(String texto) {
        btnAceptar.setText(texto);
    }

    public void setMensajePermisos(String mensaje) {
        lblPermisos.setText(mensaje == null ? "" : mensaje);
    }

    public void addAceptarListener(ActionListener listener) {
        btnAceptar.addActionListener(listener);
    }

    public void addRestaurarVersionListener(ActionListener listener) {
        btnRestaurarVersion.addActionListener(listener);
    }

    public void addFiltroListener(ActionListener listener) {
        comboFiltro.addActionListener(listener);
    }

    public void addEventoListener(ActionListener listener) {
        comboEventos.addActionListener(listener);
    }

    public void addVersionListener(ActionListener listener) {
        comboVersiones.addActionListener(listener);
    }

    public void limpiarCampos() {
        textFieldTitulo.setText("");
        textFieldSubtitulo.setText("");
        textAreaCuerpo.setText("");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EntregarReportajeView frame = new EntregarReportajeView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EntregarReportajeView() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 780, 590);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFiltro = new JLabel("Filtro de eventos");
        lblFiltro.setBounds(10, 11, 120, 14);
        contentPane.add(lblFiltro);

        comboFiltro = new JComboBox<>();
        comboFiltro.addItem("Pendientes de entrega");
        comboFiltro.addItem("Entregados");
        comboFiltro.setBounds(10, 31, 744, 22);
        contentPane.add(comboFiltro);

        JLabel lblEventos = new JLabel("Eventos asignados");
        lblEventos.setBounds(10, 64, 140, 14);
        contentPane.add(lblEventos);

        comboEventos = new JComboBox<>();
        comboEventos.setBounds(10, 84, 744, 22);
        contentPane.add(comboEventos);

        JLabel lblNewLabelTitulo = new JLabel("Título");
        lblNewLabelTitulo.setBounds(10, 117, 46, 14);
        contentPane.add(lblNewLabelTitulo);

        textFieldTitulo = new JTextField();
        textFieldTitulo.setBounds(10, 137, 744, 30);
        contentPane.add(textFieldTitulo);
        textFieldTitulo.setColumns(10);

        JLabel lblNewLabelSubtitulo = new JLabel("Subtítulo actual");
        lblNewLabelSubtitulo.setBounds(10, 178, 100, 14);
        contentPane.add(lblNewLabelSubtitulo);

        textFieldSubtitulo = new JTextField();
        textFieldSubtitulo.setBounds(10, 198, 360, 30);
        contentPane.add(textFieldSubtitulo);
        textFieldSubtitulo.setColumns(10);

        JLabel lblNewLabelCuerpo = new JLabel("Cuerpo actual");
        lblNewLabelCuerpo.setBounds(10, 239, 90, 14);
        contentPane.add(lblNewLabelCuerpo);

        textAreaCuerpo = new JTextArea();
        textAreaCuerpo.setLineWrap(true);
        textAreaCuerpo.setWrapStyleWord(true);

        JScrollPane scrollCuerpo = new JScrollPane(textAreaCuerpo);
        scrollCuerpo.setBounds(10, 259, 360, 180);
        contentPane.add(scrollCuerpo);

        JLabel lblVersiones = new JLabel("Versiones (fecha/hora y cambios)");
        lblVersiones.setBounds(394, 178, 230, 14);
        contentPane.add(lblVersiones);

        comboVersiones = new JComboBox<>();
        comboVersiones.setBounds(394, 198, 360, 22);
        contentPane.add(comboVersiones);

        JLabel lblSubtituloVersion = new JLabel("Subtítulo versión seleccionada");
        lblSubtituloVersion.setBounds(394, 239, 190, 14);
        contentPane.add(lblSubtituloVersion);

        textFieldSubtituloVersion = new JTextField();
        textFieldSubtituloVersion.setEditable(false);
        textFieldSubtituloVersion.setBounds(394, 259, 360, 30);
        contentPane.add(textFieldSubtituloVersion);
        textFieldSubtituloVersion.setColumns(10);

        JLabel lblCuerpoVersion = new JLabel("Cuerpo versión seleccionada");
        lblCuerpoVersion.setBounds(394, 300, 180, 14);
        contentPane.add(lblCuerpoVersion);

        textAreaCuerpoVersion = new JTextArea();
        textAreaCuerpoVersion.setEditable(false);
        textAreaCuerpoVersion.setLineWrap(true);
        textAreaCuerpoVersion.setWrapStyleWord(true);
        JScrollPane scrollVersion = new JScrollPane(textAreaCuerpoVersion);
        scrollVersion.setBounds(394, 320, 360, 119);
        contentPane.add(scrollVersion);

        lblPermisos = new JLabel("");
        lblPermisos.setBounds(10, 450, 560, 14);
        contentPane.add(lblPermisos);

        btnRestaurarVersion = new JButton("Restaurar versión");
        btnRestaurarVersion.setBounds(394, 475, 160, 23);
        contentPane.add(btnRestaurarVersion);

        btnAceptar = new JButton("Entregar");
        btnAceptar.setBounds(645, 475, 109, 23);
        contentPane.add(btnAceptar);
    }
}
