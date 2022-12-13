import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Main {
public static String sourcePath="";
public static String targetPath="";

	   public static void main(String[] args) {
	        JFileChooser jfcSource = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	        jfcSource.setDialogTitle("Choose your source directory to analyze: ");
	        jfcSource.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	        JFileChooser jfcTarget = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	        jfcTarget.setDialogTitle("Choose your target directory to save the results: ");
	        jfcTarget.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        
	        int returnValue1 = jfcSource.showOpenDialog(null);
	        
	        if (returnValue1 == JFileChooser.APPROVE_OPTION) {
	            if (jfcSource.getSelectedFile().isDirectory()) {
	                System.out.println("You selected the directory: " + jfcSource.getSelectedFile());
	                //int option=showOptions();
	                sourcePath=jfcSource.getSelectedFile().getAbsolutePath();
	                
	                int returnValue2 = jfcTarget.showOpenDialog(null);
	    	        if (returnValue2 == JFileChooser.APPROVE_OPTION) {	     
		                targetPath=jfcTarget.getSelectedFile().getAbsolutePath();
						cite(jfcSource.getSelectedFile().getAbsolutePath());
	                	walk(jfcSource.getSelectedFile().getAbsolutePath());
					}
	            }
	        }

	    }

	   public static int showOptions() {
		   int option=0;
		   System.out.println("Select the task to be performed from the following options");
		   System.out.println("[1] Generate .bib file with all the information of .bib file under the selected folder");
		   System.out.println("[2] Generate a .tex file citing all the sources within a .bib file");
		   System.out.println("Both options inlude nesting folders.");
		   
		   return option;

	   }
	   
	    public static void walk( String sourcePath) {
	        String output = Main.sourcePath +"\\Bibliography.bib";

	        File root = new File( sourcePath );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	                walk( f.getAbsolutePath());
	            }
	            else {
	                if (f.getPath().endsWith(".bib")){
	                    appendFiles(output, f.getAbsolutePath());
	                }
	            }
	        }
	    }

	    public static void cite( String sourcePath) {
	        String output = targetPath +"\\citing.tex";

	        File root = new File( sourcePath );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	                cite( f.getAbsolutePath() );
	            }
	            else {
	                if (f.getPath().endsWith(".bib")){
	                    citeBibliography(output, f.getAbsolutePath());
	                }
	            }
	        }
	    }

	    
	    public static void appendFiles(String permFile, String tmpFile) {
	        // Appends file "tmpFile" to the end of "permFile"
	        try {
	            // create a writer for permFile
	            BufferedWriter out = new BufferedWriter(new FileWriter(permFile, true));
	            
	            // create a reader for tmpFile
	            BufferedReader in = new BufferedReader(new FileReader(tmpFile));
	            
	            String str;
	            int start=tmpFile.lastIndexOf('\\');
	            String name=""+tmpFile.substring(start+1);
	            out.newLine();
	            out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	            out.newLine();
	            out.write("%%%%%% "+name.toUpperCase()+" %%%%%%%%");
	            out.newLine();
	            out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	            out.newLine();
	            while ((str = in.readLine()) != null) {
	                out.write(str);
	                out.newLine();
	            }
	            in.close();
	            out.close();
	        } catch (IOException e) {
	        }
	    }

	    public static void citeBibliography(String permFile, String tmpFile) {
	        // Appends file "tmpFile" to the end of "permFile"
	        try {
	            // create a writer for permFile
	            BufferedWriter out = new BufferedWriter(new FileWriter(permFile, true));
	            // create a reader for tmpFile
	            BufferedReader in = new BufferedReader(new FileReader(tmpFile));
	            String str;
	            int start=tmpFile.lastIndexOf('\\');
	            String name=""+tmpFile.substring(start+1);
	            out.newLine();
	            out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	            out.newLine();
	            out.write("%%%%%% "+name.toUpperCase()+" %%%%%%%%");
	            out.newLine();
	            out.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	            out.newLine();

	            out.write("{\\small");
	            out.newLine();
	            out.write("  \\begin{itemize}[label=\\FilledSmallSquare]");
	            out.newLine();

	            Integer idx=-1;
	            String tag="";
	            while ((str = in.readLine()) != null) {
	                if (str.startsWith("@")){
	                    idx = str.indexOf("{");
	                    tag=str.substring(1,idx);
	                    if(!tag.equalsIgnoreCase("Comment")){
	                        String bib= str.substring(idx+1).stripTrailing();
	                        bib=bib.substring(0, bib.length()-1);
	                        String citation="  \\item \\bibentry{"+bib+"}";
	                        out.write(citation);
	                        out.newLine();
	                    }
	                }
	            }

	            out.write("  \\end{itemize}");
	            out.newLine();
	            out.write("}");
	            out.newLine();

	            in.close();
	            out.close();
	        } catch (IOException e) {
	        }
	    }
}
