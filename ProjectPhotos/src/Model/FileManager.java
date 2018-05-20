package Model;

import Controller.Services.TransactionHandler;
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
 /*File outputFolder =new File(albumsDirectory+"/"+albumName);
        if(!outputFolder.exists())
            outputFolder.mkdirs();

        File outputFile = new File(outputFolder.toString()+"/"+inputFile.getName());

        FileOutputStream output;

        input=new FileInputStream(inputFile);
        output=new FileOutputStream(outputFile);
        byte[] buf=new byte[4096];
        int bytesRead;
        while((bytesRead=input.read(buf))>0)
        {
            output.write(buf,0,bytesRead);
        }
        input.close();
        output.close();
*/