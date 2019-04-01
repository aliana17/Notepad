import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.Scanner;

import javax.swing.filechooser.*;
import java.awt.datatransfer.*;
public class Notepad extends JFrame implements ActionListener{
	static JTextArea jta;
	private JMenuBar mbar;
	private JMenu mnuFile,mnuEdit,mnuFormat,mnuHelp;
	private JMenuItem mitNew,mitOpen,mitSave,mitSaveAs,mitExit,mitCut,mitCopy,mitPaste,mitDelete,mitFind,mitReplace,mitSelectAll,mitTimeDate,mitFont,mitViewHelp,mitAbout;
	private JCheckBoxMenuItem mitWordWrap;
	private Font fntDefault;
	private ImageIcon icoImage;
	private boolean saveFlag=true,newFlag=true;
	private String curFile="";//store name and path of current file
	private int n,m;
	Notepad(){	
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				int ans=JOptionPane.showConfirmDialog(Notepad.this, "Save", "Notepad", JOptionPane.YES_NO_CANCEL_OPTION);
				if(ans==JOptionPane.YES_OPTION)
					save();
				else if(ans==JOptionPane.NO_OPTION)
					dispose();
			}
		});
		mitNew=new JMenuItem("New");
		mitOpen=new JMenuItem("Open");
		mitSave=new JMenuItem("Save");
		mitSaveAs=new JMenuItem("Save As");
		mitExit=new JMenuItem("Exit");
		mitCut=new JMenuItem("Cut");
		mitCopy=new JMenuItem("Copy");
		mitPaste=new JMenuItem("Paste");
		mitDelete=new JMenuItem("Delete");
		mitFind=new JMenuItem("Find");
		mitReplace=new JMenuItem("Replace");
		mitSelectAll=new JMenuItem("Select All");
		mitTimeDate=new JMenuItem("Time Date");
		mitWordWrap=new JCheckBoxMenuItem("Word Wrap");
		mitFont=new JMenuItem("Font");
		mitViewHelp=new JMenuItem("View Help");
		mitAbout=new JMenuItem("About Notepad");
		
		mitNew.addActionListener(this);
		mitOpen.addActionListener(this);
		mitSave.addActionListener(this);
		mitSaveAs.addActionListener(this);
		mitExit.addActionListener(this);
		mitCut.addActionListener(this);
		mitCopy.addActionListener(this);
		mitPaste.addActionListener(this);
		mitDelete.addActionListener(this);
		mitFind.addActionListener(this);
		mitReplace.addActionListener(this);
		mitSelectAll.addActionListener(this);
		mitTimeDate.addActionListener(this);
		mitFont.addActionListener(this);
		mitViewHelp.addActionListener(this);
		mitAbout.addActionListener(this);
		mitWordWrap.addActionListener(this);
		
		
		mnuFile=new JMenu("File");
		mnuFile.add(mitNew);
		mnuFile.add(mitOpen);
		mnuFile.add(mitSave);
		mnuFile.add(mitSaveAs);
		mnuFile.addSeparator();
		mnuFile.add(mitExit);
		
		mnuEdit=new JMenu("Edit");
		mnuEdit.add(mitCut);
		mnuEdit.add(mitCopy);
		mnuEdit.add(mitPaste);
		mnuEdit.add(mitDelete);
		mnuEdit.addSeparator();
		mnuEdit.add(mitFind);
		mnuEdit.add(mitReplace);
		mnuEdit.addSeparator();
		mnuEdit.add(mitSelectAll);
		mnuEdit.add(mitTimeDate);
		
		mnuFormat=new JMenu("Format");
		mnuFormat.add(mitWordWrap);
		mnuFormat.add(mitFont);
		
		mnuHelp=new JMenu("Help");
		mnuHelp.add(mitViewHelp);
		mnuHelp.add(mitAbout);
	
		mbar=new JMenuBar();
		mbar.add(mnuFile);
		mbar.add(mnuEdit);
		mbar.add(mnuFormat);
		mbar.add(mnuHelp);
		
		setJMenuBar(mbar);
		
		jta=new JTextArea();
		try{
			FileInputStream fis=new FileInputStream("notepad.cfg");
			Scanner sc=new Scanner(fis);
			String fntName="";
			int fntStyle=0;
			int fntSize=0;
			fntName=sc.nextLine();
			fntStyle=Integer.parseInt(sc.nextLine());
			fntSize=Integer.parseInt(sc.nextLine());
			sc.close();
			fis.close();
			fntDefault=new Font(fntName,fntStyle,fntSize);
		}
		catch(FileNotFoundException e){
			fntDefault=new Font(Font.SERIF,Font.PLAIN,30);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		jta.setFont(fntDefault);
		jta.getDocument().addDocumentListener(new DocumentListener(){
  			public void changedUpdate(DocumentEvent e){
    			saveFlag=false;
  			}
  			public void removeUpdate(DocumentEvent e){
  				saveFlag=false;
  			}
  			public void insertUpdate(DocumentEvent e){
    			saveFlag=false;
  			}
		});
		add(new JScrollPane(jta));
		icoImage=new ImageIcon("notepad.jpg");
		setIconImage(icoImage.getImage());
		setTitle("Untitled - Notepad");
		setSize(800,800);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Notepad();
	}
	public void actionPerformed(ActionEvent ae){
		String s1=ae.getActionCommand();
		if(s1.equalsIgnoreCase("new")){
			if(saveFlag==false){
				int ans=JOptionPane.showConfirmDialog(this, "Save Changes","Notepad",JOptionPane.YES_NO_CANCEL_OPTION);
				if(ans==JOptionPane.YES_OPTION)
					save();
				else if(ans==JOptionPane.NO_OPTION){
					jta.setText("");
					setTitle("Untitled - Notepad");
					newFlag=true;
					saveFlag=true;
				}
			}
		}
		else if(s1.equalsIgnoreCase("open")){
			open();
		}
		else if(s1.equalsIgnoreCase("save")){
			save(); //overwrite existing file
		}
		else if(s1.equalsIgnoreCase("save as")){
			saveAs();//dialog file new create and save
		}
		else if(s1.equalsIgnoreCase("exit")){
			if(saveFlag==false){
				int ans=JOptionPane.showConfirmDialog(this, "Save Changes","Notepad",JOptionPane.YES_NO_CANCEL_OPTION);
				if(ans==JOptionPane.YES_OPTION)
					save();
				else 
					dispose();
			}
			else
				dispose();
		}
		else if(s1.equalsIgnoreCase("cut")){
			/*try
			{
				Robot r=new Robot();
				r.keyPress(KeyEvent.VK_CONTROL);
				r.keyPress(KeyEvent.VK_X);
				r.keyRelease(KeyEvent.VK_X);
				r.keyRelease(KeyEvent.VK_CONTROL);
			}
			catch(Exception e){}*/
			StringSelection selection=new StringSelection(jta.getSelectedText());
			Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
			clip.setContents(selection,selection);
			n=jta.getSelectionStart();
			m=jta.getSelectionEnd();
			jta.setText(jta.getText().substring(0,jta.getSelectionStart())+jta.getText().substring(jta.getSelectionEnd(),jta.getText().length()));			
			jta.setSelectionStart(n);
			jta.setSelectionEnd(n);
		}
		else if(s1.equalsIgnoreCase("copy")){
			n=jta.getSelectionStart();
			m=jta.getSelectionEnd();
			StringSelection selection=new StringSelection(jta.getSelectedText());
			Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
			clip.setContents(selection,selection);
		}
		else if(s1.equalsIgnoreCase("paste")){
			try{	
				Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable tf=clip.getContents(this);
				n=jta.getCaretPosition();
				String str=(String)tf.getTransferData(DataFlavor.stringFlavor);
				jta.setText(new StringBuffer(jta.getText()).insert(jta.getSelectionStart(),str).toString());
				jta.setSelectionStart(n + str.length());
				jta.setSelectionEnd(n + str.length());
			}
			catch(UnsupportedFlavorException|IOException e){
				e.printStackTrace();
			}	
		}
		else if(s1.equalsIgnoreCase("delete")){
			jta.setText(jta.getText().substring(0,jta.getSelectionStart())+jta.getText().substring(jta.getSelectionEnd(),jta.getText().length()));			
		}
		else if(s1.equalsIgnoreCase("find")){
			new FindDialog(this);
		}
		else if(s1.equalsIgnoreCase("replace")){
			
		}
		else if(s1.equalsIgnoreCase("select all")){
			jta.setSelectionStart(0);
			jta.setSelectionEnd(jta.getText().length());
		}
		else if(s1.equalsIgnoreCase("time date")){
			java.util.Date d1=new java.util.Date();
			SimpleDateFormat sdf=new SimpleDateFormat("hh:mm dd-MM-yyyy");
			jta.insert(sdf.format(d1),jta.getCaretPosition());
		}
		else if(s1.equalsIgnoreCase("word wrap")){
			jta.setLineWrap(mitWordWrap.getState());
		}
		else if(s1.equalsIgnoreCase("font")){
			FontChooserDialog fnt=new FontChooserDialog(this,"Font Dialog", true, fntDefault);
			fnt.setSize(300,300);
			fnt.setLocation((int)(getSize().getWidth()-300)/2,(int)(getSize().getHeight()-300)/2);
			fnt.setVisible(true);
			fntDefault=fnt.getSelectedFont();
			jta.setFont(fntDefault);
			try{
				FileOutputStream fos=new FileOutputStream("notepad.cfg");
				fos.write(fntDefault.getFontName().getBytes());
				fos.write("\r\n".getBytes());//new line
				fos.write((fntDefault.getStyle()+"").getBytes());
				fos.write("\r\n".getBytes());//new line
				fos.write((fntDefault.getSize()+"").getBytes());
				fos.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else if(s1.equalsIgnoreCase("view help")){
			
		}
		else if(s1.equalsIgnoreCase("About Notepad")){
			JDialog j=new JDialog(this,"AboutNotePad",Dialog.ModalityType.DOCUMENT_MODAL);
			j.setLayout(new BorderLayout());
			JLabel l1=new JLabel();
			ImageIcon ii=new ImageIcon("label.png");
	        ImageIcon i2=new ImageIcon(ii.getImage().getScaledInstance(326, 82, Image.SCALE_SMOOTH));
			
			l1.setIcon(i2);
			
			JLabel l2=new JLabel("NotePad For TextFile Creation",JLabel.LEFT);
		//	JLabel l3=new JLabel("                             ");
			JLabel l4=new JLabel("Version 1.0 Release Jun 2018",JLabel.LEFT);
			JLabel l5=new JLabel("This NotePad is Made on JavaSE",JLabel.LEFT);
		    JLabel l6=new JLabel(" for Document editing For",JLabel.LEFT);
			JLabel l7=new JLabel("Linux/Window Devices",JLabel.LEFT);
			JLabel l9=new JLabel("                                              ");
			j.setSize(400,400);
			JLabel l8=new JLabel();
			ImageIcon iz=new ImageIcon("ubuntu.jpg");
	        ImageIcon izz=new ImageIcon(iz.getImage().getScaledInstance(187,100, Image.SCALE_SMOOTH));
			
			l8.setIcon(izz); 
			
			JPanel p=new JPanel();
			p.add(l1);p.add(l2);/*  p.add(l3); */p.add(l4);p.add(l5); p.add(l6);p.add(l7);
			p.add(l9);
			p.add(l8);
			j.add(p);
			j.setResizable(false);
			j.setVisible(true);	
		}
	}
	void open(){
		if(saveFlag==false){
			int ans=JOptionPane.showOptionDialog(this, "Save Changes","Notepad",0,JOptionPane.QUESTION_MESSAGE,null,new String[]{"yes","Don't Save","cancel"},0);
			if(ans==0){
				save();
				return;
			}
			else if(ans==2){
				return;
			}
		}	
		JFileChooser jFileChooser=new JFileChooser("C:\\javaprog");
       	FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Text Files", "txt");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Image Files", "jpg","gif");
		jFileChooser.addChoosableFileFilter(filter1);
		jFileChooser.addChoosableFileFilter(filter2);
		jFileChooser.setFileFilter(filter1);
       	int result=jFileChooser.showOpenDialog(this);
       	if(result==JFileChooser.APPROVE_OPTION){
       		curFile=jFileChooser.getSelectedFile().getAbsolutePath();
			try{
				FileInputStream fis=new FileInputStream(curFile);
				setTitle(jFileChooser.getSelectedFile().getName()+" - Notepad");
				byte b[]=new byte[100];
				int cnt=0;
				jta.setText("");
				while((cnt=fis.read(b))!=-1){
					jta.append(new String(b,0,cnt));
				}
				fis.close();
			}
			catch(IOException e){
				JOptionPane.showMessageDialog (null,"Error in reading data!!\n\r"+e,"File Open Error",JOptionPane.ERROR_MESSAGE);
			}
			saveFlag=true;
			newFlag=false;
		}		
	}
	void save(){
		if(newFlag==true){
			saveAs();
			return;
		}
		try{
			FileOutputStream fos=new FileOutputStream(curFile);
			fos.write(jta.getText().getBytes());
			fos.close();
		}
		catch(IOException e){
			JOptionPane.showMessageDialog (null,"Error in writing data!!\n\r"+e,"File Save Error",JOptionPane.ERROR_MESSAGE);
		}
		saveFlag=true;
		newFlag=false;
	}
	void saveAs(){
		JFileChooser jFileChooser=new JFileChooser("C:\\javaprog");
       	FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Text Files", "txt");
        //FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Image Files", "jpg","gif");
		jFileChooser.addChoosableFileFilter(filter1);
		//jFileChooser.addChoosableFileFilter(filter2);
		jFileChooser.setFileFilter(filter1);
       	int result=jFileChooser.showSaveDialog(this);
       	if(result==JFileChooser.APPROVE_OPTION){
       		curFile=jFileChooser.getSelectedFile().getAbsolutePath();
			try{
				FileOutputStream fos=new FileOutputStream(curFile);
				setTitle(jFileChooser.getSelectedFile().getName()+" - Notepad");
				fos.write(jta.getText().getBytes());
				fos.close();
			}
			catch(IOException e){
				JOptionPane.showMessageDialog (null,"Error in writing data!!\n\r"+e,"File Save Error",JOptionPane.ERROR_MESSAGE);
			}
			saveFlag=true;newFlag=false;
		}
	}
}
class FindDialog extends JDialog{
	JButton btnFindNext,btnCancel;
	JLabel lblText;
	JTextField txtText;
	JRadioButton r1,r2;
	ButtonGroup bg;
	JCheckBox chkBox;
	boolean flag=false;
	FindDialog(Notepad obj){
		super(obj,"Find",false);
		setLayout(new FlowLayout());
		btnFindNext=new JButton("Find Next");
		btnFindNext.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String findWhat=txtText.getText();
				int startPos=Notepad.jta.getCaretPosition();
				boolean caseFlag=chkBox.isSelected();
				int dir;
				if(r1.isSelected())
					dir=-1;
				else
					dir=1;
				if(findWhat != null && !findWhat.equals("")){
					int findPos;
					if(dir==1){//down
						if(caseFlag==false)
							findPos=Notepad.jta.getText().toUpperCase().indexOf(findWhat.toUpperCase(),startPos);//case insensitive
						else
							findPos=Notepad.jta.getText().indexOf(findWhat,startPos);//case sensitive
					}
					else{//up
						if(flag==true)
							startPos-=(findWhat.length()+1);
						else
							flag=true;
						if(caseFlag==false)
							findPos=Notepad.jta.getText().toUpperCase().lastIndexOf(findWhat.toUpperCase(),startPos);
						else
							findPos=Notepad.jta.getText().lastIndexOf(findWhat,startPos);
						System.out.println("Find"+findPos);
					}
					if(findPos == -1){
						JOptionPane.showMessageDialog(FindDialog.this, "Text not Found");
					}
					else{
						Notepad.jta.setSelectionStart(findPos);
						Notepad.jta.setSelectionEnd(findPos+findWhat.length());
						Notepad.jta.requestFocus();
					}
				}
			}
		});
		btnCancel=new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				dispose();
			}
		});		
		bg=new ButtonGroup();
		r1=new JRadioButton("UP");
		r2=new JRadioButton("DOWN",true);
		bg.add(r1);
		bg.add(r2);
		chkBox=new JCheckBox("Match Case");
		
		lblText=new JLabel("Find What");
		txtText=new JTextField(30);
		add(lblText);
		add(txtText);
		add(btnFindNext);
		add(btnCancel);
		add(r1);
		add(r2);
		add(chkBox);
		setLocation(150,150);
		setSize(500,300);
		setVisible(true);
	}
}
class FontChooserDialog extends StandardDialog {
    private FontChooserPanel fontChooserPanel;
    public FontChooserDialog(final Dialog owner, final String title, final boolean modal, final Font font){
        super(owner, title, modal);
        setContentPane(createContent(font));
    }
	public FontChooserDialog(final Frame owner, final String title, final boolean modal, final Font font){
        super(owner, title, modal);
        setContentPane(createContent(font));
    }
	public Font getSelectedFont(){
        return this.fontChooserPanel.getSelectedFont();
    }
	private JPanel createContent(Font font){
        final JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        if(font == null){
            font = new Font("Dialog", 10, Font.PLAIN);
        }
        this.fontChooserPanel = new FontChooserPanel(font);
        content.add(this.fontChooserPanel);

        final JPanel buttons = createButtonPanel();
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        content.add(buttons, BorderLayout.SOUTH);
        return content;
    }
}
class FontChooserPanel extends JPanel{
	public static final String[] SIZES = {"9", "10", "11", "12", "14", "16",
            "18", "20", "22", "24", "28", "36", "48", "72"};
   	private JList fontlist;
   	private JList sizelist;
    private JCheckBox bold;
    private JCheckBox italic;
    public FontChooserPanel(final Font font){
        final GraphicsEnvironment g
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String[] fonts = g.getAvailableFontFamilyNames();
        setLayout(new BorderLayout());
        final JPanel right = new JPanel(new BorderLayout());
        final JPanel fontPanel = new JPanel(new BorderLayout());
        fontPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Font"));
        this.fontlist = new JList(fonts);
        final JScrollPane fontpane = new JScrollPane(this.fontlist);
        fontpane.setBorder(BorderFactory.createEtchedBorder());
        fontPanel.add(fontpane);
        add(fontPanel);

        final JPanel sizePanel = new JPanel(new BorderLayout());
        sizePanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            "Size"));
        this.sizelist = new JList(SIZES);
        final JScrollPane sizepane = new JScrollPane(this.sizelist);
        sizepane.setBorder(BorderFactory.createEtchedBorder());
        sizePanel.add(sizepane);

        final JPanel attributes = new JPanel(new GridLayout(1, 2));
        this.bold = new JCheckBox("Bold");
        this.italic = new JCheckBox("Italic");
        attributes.add(this.bold);
        attributes.add(this.italic);
        attributes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Attributes"));

        right.add(sizePanel, BorderLayout.CENTER);
        right.add(attributes, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        setSelectedFont(font);
    }
    public Font getSelectedFont(){
        return new Font(getSelectedName(), getSelectedStyle(),
                getSelectedSize());
    }
    public String getSelectedName(){
        return (String) this.fontlist.getSelectedValue();
    }
	public int getSelectedStyle(){
        if (this.bold.isSelected() && this.italic.isSelected()) {
            return Font.BOLD + Font.ITALIC;
        }
        if (this.bold.isSelected()){
            return Font.BOLD;
        }
        if (this.italic.isSelected()){
            return Font.ITALIC;
        }
        else{
            return Font.PLAIN;
        }
    }
	public int getSelectedSize(){
        final String selected = (String) this.sizelist.getSelectedValue();
        if (selected != null) {
            return Integer.parseInt(selected);
        }
        else {
            return 10;
        }
    }
    public void setSelectedFont (final Font font){
        if (font == null) {
            throw new NullPointerException();
        }
        this.bold.setSelected(font.isBold());
        this.italic.setSelected(font.isItalic());

        final String fontName = font.getName();
        ListModel model = this.fontlist.getModel();
        this.fontlist.clearSelection();
        for (int i = 0; i < model.getSize(); i++){
            if (fontName.equals(model.getElementAt(i))){
                this.fontlist.setSelectedIndex(i);
                break;
            }
        }
        final String fontSize = String.valueOf(font.getSize());
        model = this.sizelist.getModel();
        this.sizelist.clearSelection();
        for (int i = 0; i < model.getSize(); i++) {
            if (fontSize.equals(model.getElementAt(i))) {
                this.sizelist.setSelectedIndex(i);
                break;
            }
        }
    }
}
class StandardDialog extends JDialog implements ActionListener{
    private boolean cancelled;
    public StandardDialog(final Frame owner, final String title,final boolean modal){
        super(owner, title, modal);
        this.cancelled = false;
    }
    public StandardDialog(final Dialog owner, final String title,final boolean modal){
        super(owner, title, modal);
        this.cancelled = false;
    }
    public boolean isCancelled(){
        return this.cancelled;
    }
	public void actionPerformed(final ActionEvent event){
        final String command = event.getActionCommand();
        if (command.equals("helpButton")){
            // display help information
        }
        else if (command.equals("okButton")){
            this.cancelled = false;
            setVisible(false);
        }
        else if (command.equals("cancelButton")){
            this.cancelled = true;
            setVisible(false);
        }
    }
    protected JPanel createButtonPanel(){
        final L1R2ButtonPanel buttons = new L1R2ButtonPanel(
                "Help",
                "OK",
                "Cancel");

        final JButton helpButton = buttons.getLeftButton();
        helpButton.setActionCommand("helpButton");
        helpButton.addActionListener(this);

        final JButton okButton = buttons.getRightButton1();
        okButton.setActionCommand("okButton");
        okButton.addActionListener(this);

        final JButton cancelButton = buttons.getRightButton2();
        cancelButton.setActionCommand("cancelButton");
        cancelButton.addActionListener(this);

        buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        return buttons;
    }
}
class L1R2ButtonPanel extends JPanel{
	private JButton left;
	private JButton right1;
	private JButton right2;
	public L1R2ButtonPanel(final String label1, final String label2, final String label3){
        setLayout(new BorderLayout());
		this.left = new JButton(label1);
        final JPanel rightButtonPanel = new JPanel(new GridLayout(1, 2));
        this.right1 = new JButton(label2);
        this.right2 = new JButton(label3);
        rightButtonPanel.add(this.right1);
        rightButtonPanel.add(this.right2);
        add(this.left, BorderLayout.WEST);
        add(rightButtonPanel, BorderLayout.EAST);
    }
    public JButton getLeftButton(){
        return this.left;
    }
    public JButton getRightButton1(){
        return this.right1;
    }
    public JButton getRightButton2(){
        return this.right2;
    }
}
class FontDisplayField extends JTextField{
    private Font displayFont;
	public FontDisplayField(final Font font){
        super("");
        setDisplayFont(font);
        setEnabled(false);
    }
    public Font getDisplayFont(){
        return this.displayFont;
    }
	public void setDisplayFont(final Font font){
        this.displayFont = font;
        setText(fontToString(this.displayFont));
    }
    private String fontToString(final Font font){
        if (font != null){
            return font.getFontName() + ", " + font.getSize();
        }
        else{
            return "No Font Selected";
        }
    }
}
