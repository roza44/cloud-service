package helpers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import model.Organizacija;

public class FileHandler {
	private static Gson gson = new Gson();
	private static String dataFolderName = "static/data";
	private static String orgLogosFolderName = dataFolderName + File.separator + "orgLogos";
	
	private static String orgsFilename = "organizacije.json";
	
	private static void checkDir(){
		File folder = new File(dataFolderName);
		if (!folder.exists()) {
			try {
				folder.createNewFile();
			} catch (IOException e) {
				System.out.println("Data directory creation failed!");
				e.printStackTrace();
			}
		}
	}
	
	private static File newFile(String name) {
		return new File(dataFolderName + File.separator + name);
	}
	
	private static void writeString(File f, String contents) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println("File existence check failed: " + f.getName());
				e.printStackTrace();
			}
		}
		
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream(f));
			pw.write(contents);
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("Print writer creation failed!");
			e.printStackTrace();
		}
	}
	
	private static String readString(File f) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		
		String ret = null;
		try {
			ret = new String(Files.readAllBytes(f.toPath()));
		} catch (IOException e) {
			System.out.println("Reading string from file failed: " + f.getName());
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	public static void saveImage(String name, String extension, byte[] data) {
		checkDir();
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			BufferedImage image = ImageIO.read(bis);
			ImageIO.write(image, extension, newFile(name + "." + extension));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save(ArrayList<Organizacija> orgs) {
		checkDir();
		String jsonStr = gson.toJson(orgs);
		
		File f = newFile(orgsFilename);
		writeString(f, jsonStr);
	}
	
	public static ArrayList<Organizacija> loadOrgs() {
		checkDir();
		String jsonStr;
		try {
			jsonStr = readString(newFile(orgsFilename));
		} catch (FileNotFoundException e) {
			return new ArrayList<Organizacija>();
		}
		
		Organizacija[] orgArray = gson.fromJson(jsonStr, Organizacija[].class);
		ArrayList<Organizacija> ret = new ArrayList<Organizacija>();
		Collections.addAll(ret, orgArray);
		return ret;
	}
}
