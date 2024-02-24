package mvp;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Presenter implements ChangeListener {

	private Model model;
	private View view;
	private boolean isListening = true;

	public Model getModel() {
		return model;
	}

	public void updateModel() {

	}

	public void setModel(Model model) {
		this.model = model;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
		this.view.setChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if (isListening == false) {
			isListening = true;
			return;
		}
		JSpinner spinner = (JSpinner) event.getSource();
		System.out.println(spinner.getName() + ", value:" + spinner.getValue());
		int value = Integer.valueOf(spinner.getValue().toString());
		this.model.setValue(spinner.getName(), value);
		value = this.model.getValue(spinner.getName()) + 1;
		System.out.println(value);
		isListening = false;
		spinner.setValue(value);
	}

}
