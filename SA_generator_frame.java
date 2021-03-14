package sa_generator;

import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SA_generator_frame extends JFrame {

	private JPanel contentPane;
	private JTextField Size_of_SA;
	private JTextField Data_bits_of_SA;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SA_generator_frame frame = new SA_generator_frame();
					//"C:\\Users\\USER\\eclipse-workspace\\SA_generator\\SA.ico"
					frame.setIconImage(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void SA_generator(int Nsize,int Nbit) throws IOException {
		FileWriter fw = new FileWriter(System.getProperty("user.dir")+"\\SA"+Nsize+"x"+Nsize+".v");
		
		String module =  "module SA"+Nsize+"x"+Nsize+"(A_wire_"+Nsize+",A_wire_0,\r\n"
						+"            Xval_wire_"+Nsize+",Xval_wire_0,\r\n"
						+"            Xidx_wire_"+Nsize+",Xidx_wire_0,\r\n"
						+"            Xend_wire_"+(2*Nsize-1)+",Xend_wire_0,\r\n"
						+"            Wval_wire_"+Nsize+",Wval_wire_0,\r\n"
						+"            Widx_wire_"+Nsize+",Widx_wire_0,\r\n"
						+"            clk,rst_n,hold);\r\n\n";
		
		String parameter = "parameter Nbit  = " + Nbit +  "; //PE input bits\r\n"
						 + "parameter Nsize = " + Nsize + "; //PE input bits\r\n\n";
		
		String IOport = "//wire with number 0 are all input ports, all wires except Xend_wire with number "+ Nsize +" are output ports.\r\n\n"
				+ "input [("+ Nsize+"*"+ Nbit +") -1 : 0] Xval_wire_0, Wval_wire_0;   //PE X(vector input) & Weight value input port\r\n"
				+ "input [(2*"+ Nsize+"*"+ Nbit +") -1 : 0] A_wire_0;                 //PE partial sum input port, usually not used.\r\n"
				+ "input [("+ Nsize+"*"+ Nsize+") -1 : 0] Xidx_wire_0, Widx_wire_0;   //PE X(vector input) & Weight index input port\r\n"
				+ "input ["+ Nsize+"-1 : 0] Xend_wire_0;                     //X end signal input port\r\n"
				+ "input clk,rst_n,hold;\r\n"
				+ "\r\n"
				+ "output [("+ Nsize+"*"+ Nbit +")  -1 : 0] Xval_wire_"+Nsize+",Wval_wire_"+Nsize+";    //PE X(vector output) & Weight value port, usually not used. \r\n"
				+ "output [(2*"+ Nsize+"*"+ Nbit +") -1 : 0] A_wire_"+Nsize+";                  //PE partial sum output port.\r\n"
				+ "output [("+ Nsize+"*"+ Nsize+") -1 : 0] Xidx_wire_"+Nsize+",Widx_wire_"+Nsize+";     //PE X(vector input) & Weight index output port, usually not used.\r\n"
				+ "output ["+ Nsize+"-1 : 0] Xend_wire_"+(2*Nsize-1)+";                      //X end signal output port, usually not used.    \r\n\n";
		String PE;
		
		fw.write(module);
		fw.write(parameter);
		fw.write(IOport);
		
		fw.write("wire [("+ Nsize+"*"+ Nbit +") -1 : 0] ");
		for(int i = 1; i <= Nsize-2 ; i++) {
			fw.write("Xval_wire_"+i+", Wval_wire_"+i+", ");
		}
		fw.write("Xval_wire_"+(Nsize-1)+", Wval_wire_"+(Nsize-1)+";\r\n");
		
		fw.write("wire [(2*"+ Nsize+"*"+ Nbit +") -1 : 0] ");
		for(int i = 1; i <= Nsize-2 ; i++) {
			fw.write("A_wire_"+i+", ");
		}
		fw.write("A_wire_"+(Nsize-1)+";\r\n");
		
		fw.write("wire [("+ Nsize+"*"+ Nsize +") -1 : 0] ");
		for(int i = 1; i <= Nsize-2 ; i++) {
			fw.write("Xidx_wire_"+i+", Widx_wire_"+i+", ");
		}
		fw.write("Xidx_wire_"+(Nsize-1)+", Widx_wire_"+(Nsize-1)+";\r\n");
		
		fw.write("wire ");
		for(int i = 1; i <= 2*Nsize-3 ; i++) {
			fw.write("Xend_wire_"+i+", ");
		}
		fw.write("Xend_wire_"+(2*Nsize-2)+";\r\n\n");
		
		for(int r = 1; r <= Nsize; r++) {
			for(int c = 1; c <= Nsize; c++) {
				PE = "PE PE" + r + "_" + c + "(.Ain_in(A_wire_"+ (c-1) +"["+(r*2*Nbit-1)+":"+((r-1)*2*Nbit)+"]),.Aout(A_wire_"+ c +"["+(r*2*Nbit-1)+":"+((r-1)*2*Nbit)+"]),.Aout_en(),\r\n"
						+ "        .Xval_in(Xval_wire_"+ (r-1) +"["+(c*Nbit-1)+":"+((c-1)*Nbit)+"]),.Xval_out(Xval_wire_"+ r +"["+(c*Nbit-1)+":"+((c-1)*Nbit)+"]),\r\n"
						+ "        .Xidx_in(Xidx_wire_"+ (r-1) +"["+(c*Nsize-1)+":"+((c-1)*Nsize)+"]),.Xidx_out(Xidx_wire_"+ r +"["+(c*Nsize-1)+":"+((c-1)*Nsize)+"]),\r\n"
						+ "        .Xend_in(Xend_wire_"+ (c+r-2) +"),.Xend_out(Xend_wire_"+ (c+r-1) +"),\r\n"
						+ "        .Wval_in(Wval_wire_"+ (c-1) +"["+(r*Nbit-1)+":"+((r-1)*Nbit)+"]),.Wval_out(Wval_wire_"+ c +"["+(r*Nbit-1)+":"+((r-1)*Nbit)+"]),\r\n"
						+ "        .Widx_in(Widx_wire_"+ (c-1) +"["+(r*Nsize-1)+":"+((r-1)*Nsize)+"]),.Widx_out(Widx_wire_"+ c +"["+(r*Nsize-1)+":"+((r-1)*Nsize)+"]),\r\n"
						+ "        .clk(clk),.rst_n(rst_n),.hold(hold));\r\n\n";
				fw.write(PE);
			}
			
		}
		fw.write("endmodule");
		fw.flush();
		fw.close();
	}

	// Create the frame.
	public SA_generator_frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 300, 320, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Size_of_SA = new JTextField();
		Size_of_SA.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		Size_of_SA.setText("4");
		Size_of_SA.setBounds(180, 25, 85, 21);
		contentPane.add(Size_of_SA);
		Size_of_SA.setColumns(10);
		
		Data_bits_of_SA = new JTextField("8");
		Data_bits_of_SA.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		Data_bits_of_SA.setBounds(180, 75, 85, 21);
		contentPane.add(Data_bits_of_SA);
		Data_bits_of_SA.setColumns(10);
		
		JLabel Size_of_SA_text = new JLabel("Size of SA");
		Size_of_SA_text.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		Size_of_SA_text.setHorizontalAlignment(SwingConstants.RIGHT);
		Size_of_SA_text.setBounds(53, 30, 77, 15);
		contentPane.add(Size_of_SA_text);
		
		JLabel Data_bits_of_SA_text = new JLabel("Data bits of SA");
		Data_bits_of_SA_text.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		Data_bits_of_SA_text.setHorizontalAlignment(SwingConstants.RIGHT);
		Data_bits_of_SA_text.setBounds(25, 80, 113, 15);
		contentPane.add(Data_bits_of_SA_text);
		
		JButton btnNewButton = new JButton("Generate SA");
		btnNewButton.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SA_generator_frame frame = new SA_generator_frame();
				try {
					frame.SA_generator(Integer.parseInt(Size_of_SA.getText()), Integer.parseInt(Data_bits_of_SA.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnNewButton.setBounds(20, 128, 125, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Generate TB");
		btnNewButton_1.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(160, 128, 125, 25);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Generate BOTH!!");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		btnNewButton_2.setBounds(20, 170, 265, 23);
		contentPane.add(btnNewButton_2);
	}
}
