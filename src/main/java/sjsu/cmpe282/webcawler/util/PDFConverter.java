package sjsu.cmpe282.webcawler.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFConverter {

	public static void main(String[] args) throws Exception {
		converTo("D:\\Documents\\KuaiPan\\resume-dataset-pdf",
				"D:\\Documents\\KuaiPan\\resume-dataset-txt");
	}

	public static void converTo(String source, String dest) throws Exception{
		File sourceDir = new File(source);
		File destDir = new File(dest);
		if (!sourceDir.exists())
			throw new Exception("source not exist!");
		if (!destDir.exists())
			destDir.mkdir();
		for (File f : sourceDir.listFiles()) {
			System.out.println("converting " + f.getName());
			try {
				PDDocument pdf = PDDocument.load(f);
				PDFTextStripper stripper = new PDFTextStripper();
				String plainText = stripper.getText(pdf);
				FileWriter fw = new FileWriter(dest+"\\"+f.getName().replace(".pdf", ".txt").trim());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(plainText);
				bw.close();
				pdf.close();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
	
}
