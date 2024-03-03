package mvi;

public class MVIApp {

	public static void main(String[] args) {

		try {
			Model model = new Model("001");
			View view = new View();
			view.setName("001");
			view.setVisible(true);
			model.setView(view);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
