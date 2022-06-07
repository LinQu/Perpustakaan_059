package perpustakaan059;

import connection059.DBConnect;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class JasperReport extends JFrame {
    private JPanel Jasper;
    private JButton button1;
    DBConnect Connection = new DBConnect();

    public static void main(String[] args) {
        new JasperReport().setVisible(true);
    }
    public JasperReport() {
        setContentPane(Jasper);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JasperPrint JPrint;

                Map param = new HashMap();
                try{
                    JPrint = JasperFillManager.fillReport("Report/Report.jasper",param,Connection.conn);
                    JasperViewer viewer = new JasperViewer(JPrint,false);
                    viewer.setTitle("Laporan");
                    viewer.setVisible(true);
                }catch (JRException e2){
                    System.out.println(e2.getMessage());
                }
            }
        });
    }
}
