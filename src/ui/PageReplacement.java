package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import os.PageStrings;

public class PageReplacement extends javax.swing.JFrame {
    
    DefaultComboBoxModel page_mdl = new DefaultComboBoxModel();
    DefaultComboBoxModel string_mdl = new DefaultComboBoxModel();
    DefaultComboBoxModel algo_mdl = new DefaultComboBoxModel();
    
    ArrayList<TextCell> inputList = new ArrayList<TextCell>();
    ArrayList<Object> randomList = new ArrayList<Object>();
    
    int hitCount = 0;
    int faultCount = 0;
    
    public PageReplacement() {
        super("Page Replacement Algorithms");
        initComponents();
        
        this.remove(pnl_main);
        this.remove(pnl_controls);
        this.remove(pnl_result);
        
        setPanelSize(pnl_controls, new Dimension(970, 115));
        setPanelSize(pnl_main, new Dimension(970, 350));
        setPanelSize(pnl_input, new Dimension(944, 50));
        setPanelSize(pnl_result, new Dimension(970, 80));
        pnl_main.setLayout(new GridLayout(1, 60, 0, 0));
        set_inputs(10, "random");
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        this.add(pnl_controls);
        this.add(pnl_main);
        this.add(pnl_result);
        
        set_combos();
        setAbsoluteSize(new Dimension(987, 597));
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.white);
        setVisible(true);
    }
    
    public void set_inputs(int length, String mode) {
        inputList = new ArrayList<TextCell>();
        randomList = new ArrayList<Object>();
        
        pnl_controls.remove(pnl_input);
        pnl_input.removeAll();
        pnl_input.setLayout(new GridLayout(1, length, 0, 0));
        for (int x = 1; x <= length; x++) {
            if (mode.equalsIgnoreCase("RANDOM")) {
                int randomdigit = (int) (Math.random() * 10);
                randomList.add(randomdigit);
                pnl_input.add(new MemoryCell(25, Integer.toString(randomdigit)));
            } else {
                TextCell tc = new TextCell(25, "0");
                pnl_input.add(tc);
                inputList.add(tc);
                
            }
            pnl_input.add(new BlockCell(25));
        }
        pnl_controls.add(pnl_input);
    }
    
    public void execute(int algorithm) {
        hitCount = 0;
        faultCount = 0;
        try {
            if (cmd_random.getText().equalsIgnoreCase("randomize")) {
                
            } else {
                randomList = new ArrayList<Object>();
                for (TextCell tc : inputList) {
                    randomList.add(Integer.parseInt(tc.getText()));
                }
            }
            // System.out.println(randomList.toString());
            PageStrings ps = new PageStrings(randomList, (cmb_pages.getSelectedIndex() + 3));
            if (algorithm == 0) {
                ps.fifo_Counter();
            } else if (algorithm == 1) {
                ps.fifo_Stack();
            } else if (algorithm == 2) {
                ps.lru_Counter();
            } else if (algorithm == 3) {
                ps.lru_Stack();
            } else if (algorithm == 4) {
                ps.optimal_page_replacement();
            }
            ps.fix_index();
            this.remove(pnl_result);
            this.remove(pnl_main);
            int rows = (cmb_pages.getSelectedIndex() + 5);
            int cols = Integer.parseInt(cmb_length.getSelectedItem().toString());
            pnl_main.removeAll();
            pnl_main.setLayout(new GridLayout(rows, (cols * 2), 0, 0));
            
            for (int x = 0; x < cols; x++) {
                pnl_main.add(new HeaderCell(25, randomList.get(x).toString(), Color.white));
                pnl_main.add(new BlockCell(25));
            }
//--------------------------------------------------------------

            for (int out = 0; out < (rows - 2); out++) {
                for (int x = 0; x < cols; x++) {
                    
                    try {
                        String s = ps.getMemoryFrame().get(x).getMemoryFrame().get(out).toString();
                        String d = ps.getMemoryFrame().get(x).getCandidate().toString();
                        String asterisk = "";
                        if (algorithm == 0 || algorithm == 2 || algorithm == 4) {
                            asterisk = "*";
                        }
                        if (s.equalsIgnoreCase(d)) {
                            pnl_main.add(new MemoryCell(25, ps.getMemoryFrame().get(x).getMemoryFrame().get(out).toString() + asterisk, new Color(255, 153, 153)));
                        } else {
                            pnl_main.add(new MemoryCell(25, ps.getMemoryFrame().get(x).getMemoryFrame().get(out).toString()));
                        }
                        
                    } catch (Exception e) {
                        pnl_main.add(new MemoryCell(25, ""));
                    }
                    pnl_main.add(new BlockCell(25));
                }
            }

            //------------------------------------------------
            for (int x = 0; x < cols; x++) {
                String res = "H";
                Color c = Color.white;
                if (ps.getMemoryFrame().get(x).getResult()) {
                    res = "H";
                    hitCount++;
                    c = new Color(204, 255, 204);
                } else {
                    res = "F";
                    faultCount++;
                    c = new Color(255, 204, 204);
                }
                pnl_main.add(new HeaderCell(25, res, c));
                pnl_main.add(new BlockCell(25));
            }
            
            this.add(pnl_main);
            this.add(pnl_result);
            DecimalFormat dc = new DecimalFormat("0.00");
            DecimalFormat pc = new DecimalFormat("0");
            
            lbl_hit.setText(String.valueOf(hitCount));
            lbl_fault.setText(String.valueOf(faultCount));
            
            double sum = hitCount + faultCount;
            lbl_hitratio.setText(dc.format(hitCount / sum));
            bl_faultratio.setText(dc.format(faultCount / sum));
            
            lbl_hitper.setText(pc.format((hitCount / sum) * 100) + " %");
            lbl_faultper.setText(pc.format((faultCount / sum) * 100) + " %");
            pnl_main.revalidate();
            pnl_result.revalidate();
            this.revalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void set_combos() {
        for (int x = 3; x <= 5; x++) {
            page_mdl.addElement(x + " Pages");
        }
        cmb_pages.setModel(page_mdl);
        
        for (int x = 10; x <= 25; x++) {
            string_mdl.addElement(x);
        }
        cmb_length.setModel(string_mdl);
        
        algo_mdl.addElement("FIFO Counter");
        algo_mdl.addElement("FIFO Stack");
        algo_mdl.addElement("LRU Counter");
        algo_mdl.addElement("LRU Stack");
        algo_mdl.addElement("OPT Counter");
        
        cmb_algo.setModel(algo_mdl);
    }
    
    public void setAbsoluteSize(Dimension d) {
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setSize(d);
        this.setPreferredSize(d);
    }
    
    public void setPanelSize(JPanel p, Dimension d) {
        p.setMaximumSize(d);
        p.setMinimumSize(d);
        p.setSize(d);
        p.setPreferredSize(d);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_result = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_hit = new javax.swing.JLabel();
        lbl_fault = new javax.swing.JLabel();
        lbl_hitratio = new javax.swing.JLabel();
        bl_faultratio = new javax.swing.JLabel();
        lbl_hitper = new javax.swing.JLabel();
        lbl_faultper = new javax.swing.JLabel();
        pnl_controls = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmb_pages = new javax.swing.JComboBox();
        cmb_length = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmb_algo = new javax.swing.JComboBox();
        cmb_source = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmd_random = new javax.swing.JButton();
        pnl_input = new javax.swing.JPanel();
        pnl_main = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(600, 1000));
        setMinimumSize(new java.awt.Dimension(600, 1000));
        setPreferredSize(new java.awt.Dimension(600, 1000));

        pnl_result.setBackground(new java.awt.Color(255, 255, 255));
        pnl_result.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel5.setText("Page Hit:");

        jLabel6.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel6.setText("Page Fault: ");

        jLabel7.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel7.setText("Page Hit Ratio:");

        jLabel8.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel8.setText("Page Fault Ratio:");

        jLabel9.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel9.setText("Page Hit Percentage:");

        jLabel10.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel10.setText("Page Fault Percentage:");

        jLabel11.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 102, 102));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("IT 333 IT || Mr. Eddinel Valentino || Panganiban, Lyka Louis G. || Perello, Jhon Melvin N. || BSIT 3A-G1");

        lbl_hit.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lbl_hit.setText("0");

        lbl_fault.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lbl_fault.setText("0");

        lbl_hitratio.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lbl_hitratio.setText("0.00");

        bl_faultratio.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        bl_faultratio.setText("0.00");

        lbl_hitper.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lbl_hitper.setText("0 %");

        lbl_faultper.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lbl_faultper.setText("0 %");

        javax.swing.GroupLayout pnl_resultLayout = new javax.swing.GroupLayout(pnl_result);
        pnl_result.setLayout(pnl_resultLayout);
        pnl_resultLayout.setHorizontalGroup(
            pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_resultLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_resultLayout.createSequentialGroup()
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_fault, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_hit, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(56, 56, 56)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bl_faultratio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_hitratio, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_faultper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_hitper, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnl_resultLayout.setVerticalGroup(
            pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_resultLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnl_resultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_resultLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(pnl_resultLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_resultLayout.createSequentialGroup()
                                .addComponent(lbl_hit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_fault)))
                        .addGroup(pnl_resultLayout.createSequentialGroup()
                            .addComponent(lbl_hitratio)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bl_faultratio))
                        .addGroup(pnl_resultLayout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel10)))
                    .addGroup(pnl_resultLayout.createSequentialGroup()
                        .addComponent(lbl_hitper)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_faultper)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        pnl_controls.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel1.setText("Page Capacity");

        cmb_pages.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        cmb_pages.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_pages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_pagesActionPerformed(evt);
            }
        });

        cmb_length.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        cmb_length.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_length.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_lengthActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel2.setText("String Length");

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel3.setText("Algorithm");

        cmb_algo.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        cmb_algo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_algo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_algoActionPerformed(evt);
            }
        });

        cmb_source.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        cmb_source.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Random", "Input" }));
        cmb_source.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_sourceActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel4.setText("Data Source");

        cmd_random.setBackground(new java.awt.Color(204, 204, 255));
        cmd_random.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        cmd_random.setText("Randomize");
        cmd_random.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_randomActionPerformed(evt);
            }
        });

        pnl_input.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnl_inputLayout = new javax.swing.GroupLayout(pnl_input);
        pnl_input.setLayout(pnl_inputLayout);
        pnl_inputLayout.setHorizontalGroup(
            pnl_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnl_inputLayout.setVerticalGroup(
            pnl_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_controlsLayout = new javax.swing.GroupLayout(pnl_controls);
        pnl_controls.setLayout(pnl_controlsLayout);
        pnl_controlsLayout.setHorizontalGroup(
            pnl_controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_controlsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_controlsLayout.createSequentialGroup()
                        .addComponent(pnl_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnl_controlsLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_pages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_length, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cmb_algo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(cmb_source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmd_random, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnl_controlsLayout.setVerticalGroup(
            pnl_controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_controlsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmb_pages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmb_length, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cmb_algo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmb_source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmd_random))
                .addGap(18, 18, 18)
                .addComponent(pnl_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pnl_main.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnl_mainLayout = new javax.swing.GroupLayout(pnl_main);
        pnl_main.setLayout(pnl_mainLayout);
        pnl_mainLayout.setHorizontalGroup(
            pnl_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 963, Short.MAX_VALUE)
        );
        pnl_mainLayout.setVerticalGroup(
            pnl_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_controls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_result, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_controls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(pnl_result, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmb_sourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_sourceActionPerformed
        // TODO add your handling code here:
        if (cmb_source.getSelectedItem().toString().equalsIgnoreCase("RANDOM")) {
            cmd_random.setText("Randomize");
            set_inputs(Integer.parseInt(cmb_length.getSelectedItem().toString()), cmb_source.getSelectedItem().toString());
            execute(cmb_algo.getSelectedIndex());
        } else {
            cmd_random.setText("Execute");
            set_inputs(Integer.parseInt(cmb_length.getSelectedItem().toString()), cmb_source.getSelectedItem().toString());
            execute(cmb_algo.getSelectedIndex());
        }
        this.revalidate();
        

    }//GEN-LAST:event_cmb_sourceActionPerformed

    private void cmb_lengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_lengthActionPerformed
        // TODO add your handling code here:

        set_inputs(Integer.parseInt(cmb_length.getSelectedItem().toString()), cmb_source.getSelectedItem().toString());
        this.revalidate();
        execute(cmb_algo.getSelectedIndex());
    }//GEN-LAST:event_cmb_lengthActionPerformed

    private void cmd_randomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_randomActionPerformed
        // TODO add your handling code here:
        try {
            if (cmd_random.getText().equalsIgnoreCase("randomize")) {
                set_inputs(Integer.parseInt(cmb_length.getSelectedItem().toString()), cmb_source.getSelectedItem().toString());
                this.revalidate();
            }
            execute(cmb_algo.getSelectedIndex());
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cmd_randomActionPerformed

    private void cmb_pagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_pagesActionPerformed
        // TODO add your handling code here:
        execute(cmb_algo.getSelectedIndex());
    }//GEN-LAST:event_cmb_pagesActionPerformed

    private void cmb_algoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_algoActionPerformed
        // TODO add your handling code here:
        execute(cmb_algo.getSelectedIndex());
    }//GEN-LAST:event_cmb_algoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PageReplacement();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bl_faultratio;
    private javax.swing.JComboBox cmb_algo;
    private javax.swing.JComboBox cmb_length;
    private javax.swing.JComboBox cmb_pages;
    private javax.swing.JComboBox cmb_source;
    private javax.swing.JButton cmd_random;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl_fault;
    private javax.swing.JLabel lbl_faultper;
    private javax.swing.JLabel lbl_hit;
    private javax.swing.JLabel lbl_hitper;
    private javax.swing.JLabel lbl_hitratio;
    private javax.swing.JPanel pnl_controls;
    private javax.swing.JPanel pnl_input;
    private javax.swing.JPanel pnl_main;
    private javax.swing.JPanel pnl_result;
    // End of variables declaration//GEN-END:variables
}

class MemoryCell extends JPanel {
    
    JLabel content = new JLabel();
    
    MemoryCell(int size, String content) {
        this.content.setText(content);
        this.content.setFont(new Font("courier new,", Font.PLAIN, 14));
        this.content.setHorizontalAlignment(JLabel.CENTER);
        
        this.setAbsoluteSize(new Dimension(size, size));
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(this.content);
        setBackground(Color.white);
    }
    
    MemoryCell(int size, String content, Color c) {
        this.content.setText(content);
        this.content.setFont(new Font("courier new,", Font.PLAIN, 14));
        this.content.setHorizontalAlignment(JLabel.CENTER);
        
        this.setAbsoluteSize(new Dimension(size, size));
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(this.content);
        setBackground(c);
    }
    
    public void setAbsoluteSize(Dimension d) {
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setSize(d);
    }
}

class HeaderCell extends JPanel {
    
    JLabel content = new JLabel();
    
    HeaderCell(int size, String content, Color c) {
        this.content.setText(content);
        this.content.setFont(new Font("courier new,", Font.PLAIN, 14));
        this.content.setHorizontalAlignment(JLabel.CENTER);
        
        this.setAbsoluteSize(new Dimension(size, size));
        this.setLayout(new GridLayout(1, 1, 0, 0));
        //this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(this.content);
        setBackground(c);
    }
    
    public void setAbsoluteSize(Dimension d) {
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setSize(d);
    }
}

class BlockCell extends JPanel {
    
    BlockCell(int size) {
        this.setAbsoluteSize(new Dimension(size, size));
        setBackground(Color.white);
    }
    
    public void setAbsoluteSize(Dimension d) {
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setSize(d);
    }
}

class TextCell extends JPanel implements KeyListener {
    
    JTextField content = new JTextField();
    
    TextCell(int size, String content) {
        this.content.setText(content);
        this.content.setFont(new Font("courier new,", Font.PLAIN, 14));
        this.content.setHorizontalAlignment(JLabel.CENTER);
        this.content.addKeyListener(this);
        
        this.setAbsoluteSize(new Dimension(size, size));
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(this.content);
        setBackground(Color.white);
    }
    
    public void setAbsoluteSize(Dimension d) {
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setSize(d);
    }
    
    public String getText() {
        return this.content.getText();
    }
    
    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        if (this.content.getText().length() > 1) {
            this.content.setText(Character.toString(this.content.getText().charAt(0)));
        }
        try {
            if (Character.isDigit(this.content.getText().charAt(0))) {
            } else {
                this.content.setText("");
            }
        } catch (Exception ex) {
        }
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        this.content.setText("");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
