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

import model.Disk;
import model.KategorijaVM;
import model.Korisnik;
import model.Organizacija;
import model.VirtualnaMasina;

public class FileHandler {
	private static Gson gson = new Gson();
	private static String dataFolderName = "static/data";
	
	private static String disksFilename = "disks.json";
	private static String orgsFilename = "organizacije.json";
	private static String catsFilename = "cats.json";
	private static String vmsFilename = "vms.json";
	private static String usersFilename = "users.json";
	
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
	
	public static void saveOrgs(ArrayList<Organizacija> orgs) {
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
		
		Organizacija[] _array = gson.fromJson(jsonStr, Organizacija[].class);
		ArrayList<Organizacija> ret = new ArrayList<Organizacija>();
		Collections.addAll(ret, _array);
		return ret;
	}
	
	public static void saveDisks(ArrayList<Disk> disks) {
		checkDir();
		String jsonStr = gson.toJson(disks);
		
		File f = newFile(disksFilename);
		writeString(f, jsonStr);
	}
	
	public static ArrayList<Disk> loadDisks() {
		checkDir();
		String jsonStr;
		try {
			jsonStr = readString(newFile(disksFilename));
		} catch (FileNotFoundException e) {
			return new ArrayList<Disk>();
		}
		
		Disk[] _array = gson.fromJson(jsonStr, Disk[].class);
		ArrayList<Disk> ret = new ArrayList<Disk>();
		Collections.addAll(ret, _array);
		return ret;
	}
	
	public static void saveCats(ArrayList<KategorijaVM> cats) {
		checkDir();
		String jsonStr = gson.toJson(cats);
		
		File f = newFile(catsFilename);
		writeString(f, jsonStr);
	}
	
	public static ArrayList<KategorijaVM> loadCats() {
		checkDir();
		String jsonStr;
		try {
			jsonStr = readString(newFile(catsFilename));
		} catch (FileNotFoundException e) {
			return new ArrayList<KategorijaVM>();
		}
		
		KategorijaVM[] _array = gson.fromJson(jsonStr, KategorijaVM[].class);
		ArrayList<KategorijaVM> ret = new ArrayList<KategorijaVM>();
		Collections.addAll(ret, _array);
		return ret;
	}
	
	public static void saveVMs(ArrayList<VirtualnaMasina> vms) {
		checkDir();
		String jsonStr = gson.toJson(vms);
		
		File f = newFile(vmsFilename);
		writeString(f, jsonStr);
	}
	
	public static ArrayList<VirtualnaMasina> loadVMs() {
		checkDir();
		String jsonStr;
		try {
			jsonStr = readString(newFile(vmsFilename));
		} catch (FileNotFoundException e) {
			return new ArrayList<VirtualnaMasina>();
		}
		
		VirtualnaMasina[] _array = gson.fromJson(jsonStr, VirtualnaMasina[].class);
		ArrayList<VirtualnaMasina> ret = new ArrayList<VirtualnaMasina>();
		Collections.addAll(ret, _array);
		return ret;
	}
	
	public static void saveUsers(ArrayList<Korisnik> users) {
		checkDir();
		String jsonStr = gson.toJson(users);
		
		File f = newFile(usersFilename);
		writeString(f, jsonStr);
	}
	
	public static ArrayList<Korisnik> loadUsers() {
		checkDir();
		String jsonStr;
		try {
			jsonStr = readString(newFile(usersFilename));
		} catch (FileNotFoundException e) {
			return new ArrayList<Korisnik>();
		}
		
		Korisnik[] _array = gson.fromJson(jsonStr, Korisnik[].class);
		ArrayList<Korisnik> ret = new ArrayList<Korisnik>();
		Collections.addAll(ret, _array);
		return ret;
	}
}
