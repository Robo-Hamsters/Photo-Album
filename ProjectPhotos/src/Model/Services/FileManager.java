package Model.Services;

import Controller.Services.TransactionHandler;
import Model.Photo;
import Repo.DBConnector;
import Repo.PhotoRepo;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class FileManager extends TransactionHandler {
    private File file;
    private Photo inputFile;

    public File getFile() {
        return file;
    }

    public boolean hasFile()
    {
        return file != null;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void chooseImage()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        FileChooser.ExtensionFilter imageFilter=new FileChooser.ExtensionFilter("Image Files","*.png","*.jpeg","*.jpg");

        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);

        file = fileChooser.showOpenDialog(null);
    }

    public void saveFile(Photo inputFile) throws IOException {

        this.inputFile = inputFile;

        byte[] imageBytes = new byte[(int)file.length()];
        try{
            FileInputStream input = new FileInputStream(file);
            input.read(imageBytes);
            input.close();
            this.inputFile.setImage(imageBytes);
        }catch(Exception e){

        }

        this.inputFile.setIdPhoto(UUID.randomUUID());
        createTransaction();
    }

    @Override
    public void task(DBConnector con) {
        PhotoRepo repo=new PhotoRepo();
        repo.dbInsertPhoto(inputFile,con);

    }
}