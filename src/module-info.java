module TIC {
	requires javafx.controls;
	requires javafx.media;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
