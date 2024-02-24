package mvp;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JSpinner spinner001;
	private JSpinner spinner003;
	private JSpinner spinner002;

	/**
	 * Create the frame.
	 */
	public View() {
		setTitle("MVP: Model-View-Presenter");
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

		Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setLocation(centerPoint.x - (int) this.getSize().getWidth() / 2,
				centerPoint.y - (int) this.getSize().getHeight() / 2);
		
		
	}

	public void setChangeListener(ChangeListener presenter) {
		spinner001.addChangeListener(presenter);
		spinner002.addChangeListener(presenter);
		spinner003.addChangeListener(presenter);
	}

}
