package mvvm;

public class MVVMApp {

	public static void main(String[] args) {

		try {
			Model model = new Model("Model-1");

			ViewModel viewModel = new ViewModel();
			viewModel.setModel(model);
			
			viewModel.getViewModelProperties().add(new ViewModelProperty("Property-1", viewModel));
			viewModel.getViewModelProperties().add(new ViewModelProperty("Property-2", viewModel));
			viewModel.getViewModelProperties().add(new ViewModelProperty("Property-3", viewModel));

			View view = new View();
			view.setVisible(true);
			
			view.bind(viewModel);
		} catch (
		Exception e) {
			e.printStackTrace();
		}
	}

}
