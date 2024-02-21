package mvvm;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.SpinnerNumberModel;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private List<Binder> bindList = new ArrayList<>();
	private JSpinner spinner001;
	private JSpinner spinner003;
	private JSpinner spinner002;
	private JSpinner spinnerX;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					View frame = new View();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public View() {
		setTitle("MVVM: Model-View-ViewModel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		spinner001 = new JSpinner();
		spinner001.setName("spinner001");
		((JSpinner.DefaultEditor) spinner001.getEditor()).getTextField().setColumns(3);
		spinner001.setFont(new Font("Dialog", Font.BOLD, 32));
		contentPane.add(spinner001);
		
		spinner002 = new JSpinner();
		spinner002.setName("spinner002");
		((JSpinner.DefaultEditor) spinner002.getEditor()).getTextField().setColumns(3);
		spinner002.setFont(new Font("Dialog", Font.BOLD, 32));
		contentPane.add(spinner002);
		
		spinner003 = new JSpinner();
		spinner003.setName("spinner003");
		((JSpinner.DefaultEditor) spinner003.getEditor()).getTextField().setColumns(3);
		spinner003.setFont(new Font("Dialog", Font.BOLD, 32));
		contentPane.add(spinner003);
		
		spinnerX = new JSpinner();
		spinnerX.setModel(new SpinnerNumberModel(99	, null, null, Integer.valueOf(2)));
		((JSpinner.DefaultEditor) spinnerX.getEditor()).getTextField().setColumns(5);
		spinnerX.setName("spinnerX");
		spinnerX.setFont(new Font("Courier", Font.BOLD, 40));
		contentPane.add(spinnerX);

		Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setLocation(centerPoint.x - (int) this.getSize().getWidth() / 2,
				centerPoint.y - (int) this.getSize().getHeight() / 2);
	}
	
	public void bind(ViewModel viewModel) {
		
			bindList.add(new Binder(spinner001, viewModel.getViewModelProperties().get(0)));
			bindList.add(new Binder(spinner002, viewModel.getViewModelProperties().get(1)));
			bindList.add(new Binder(spinner003, viewModel.getViewModelProperties().get(2)));
			new Binder(spinnerX, viewModel.getPropertyX());
		
	}

}
