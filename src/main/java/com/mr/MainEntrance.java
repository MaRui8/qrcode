package com.mr;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mr
 * @date 2019/11/8
 */
public class MainEntrance {

    List<BufferedImage> bufferedImages = new ArrayList<>(1);
    JFrame jFrame = new JFrame("二维码生成器");
    JTextArea textArea = new JTextArea();
    JButton button = new JButton("生成");
    JPanel panel = new JPanel();
    JLabel imageLabel = new JLabel();
    JFileChooser fileChooser = new JFileChooser();
    public MainEntrance() {
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridBagLayout());
        textArea.setLineWrap(true);
        textArea.setBorder(new TitledBorder("文本"));

        imageLabel.setBorder(new TitledBorder("图片"));
        ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB));
        imageLabel.setIcon(imageIcon);
        PopupMenu popupMenu = new PopupMenu();
        MenuItem save = new MenuItem("保存");
        MenuItem read = new MenuItem("识别");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("图片(.png)","png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("图片(.jpg)","jpg"));
        save.addActionListener(e->{
            fileChooser.showSaveDialog(imageLabel);
            String suffix = ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0];
            try {
                if(bufferedImages.size()>0 && null !=fileChooser.getSelectedFile()) {
                    ImageIO.write(bufferedImages.get(0), suffix, new File(fileChooser.getSelectedFile().getAbsolutePath() + "."+suffix));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        popupMenu.add(save);
        popupMenu.add(read);
        imageLabel.add(popupMenu);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3 && bufferedImages.size()>0){
                    popupMenu.show(imageLabel,e.getX(),e.getY());
                }
            }
        });

        button.addActionListener(e -> {
            bufferedImages.clear();
            BufferedImage bitMatrixImage = QRCodeDemo.getBitMatrixImage(textArea.getText().trim());
            bufferedImages.add(bitMatrixImage);
            imageIcon.setImage(bitMatrixImage);
            imageLabel.repaint();
        });

        jFrame.add(textArea, new GridBagConstraints(0, 0, 3, 3, 50, 50, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jFrame.add(button, new GridBagConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        jFrame.add(imageLabel, new GridBagConstraints(4, 0, 3, 3, 100, 100, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new MainEntrance();
//识别中间带头像的二维码
//        BufferedImage bufferedImage = ImageIO.read(new File("D:/软件E盘/Java_Code/二维码/bin/12.png"));
//        System.out.println(QrCodeUtil.readToString(bufferedImage));
    }
}
