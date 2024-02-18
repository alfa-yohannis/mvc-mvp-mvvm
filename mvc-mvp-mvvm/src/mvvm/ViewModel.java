package mvvm;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {

	private Model model;
	private List<ViewModelProperty> viewModelProperties = new ArrayList<>();

	public Model getModel() {
		return model;
	}

	public void updateModel() {

	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<ViewModelProperty> getViewModelProperties() {
		return viewModelProperties;
	}

	public void setViewModelProperties(List<ViewModelProperty> viewModelProperties) {
		this.viewModelProperties = viewModelProperties;
	}

	public void onPropertyChanged(ViewModelProperty viewModelProperty) {
		System.out.println(viewModelProperty.getName() + ", value:" + viewModelProperty.getValue());
		int value = Integer.valueOf(viewModelProperty.getValue().toString());
		this.model.setValue(viewModelProperty.getName(), value);
		value = this.model.getValue(viewModelProperty.getName()) + 1;
		viewModelProperty.setValue(value);
		
	}

}
