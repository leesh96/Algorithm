import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class Vertex {
    public int vertexNum, xPoint, yPoint;

    public Vertex(int vertexNum, int xPoint, int yPoint) {
        this.vertexNum = vertexNum;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public int getxPoint() {
        return xPoint;
    }

    public void setxPoint(int xPoint) {
        this.xPoint = xPoint;
    }

    public int getyPoint() {
        return yPoint;
    }

    public void setyPoint(int yPoint) {
        this.yPoint = yPoint;
    }
}

class Edge {
    public Vertex start, end;
    public int weight;
    public boolean isVisited;

    public Edge(Vertex start, Vertex end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.isVisited = false;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}

public class GraphFrame extends JFrame {
    final int MAX_VERTEX = 10;

    public DrawPanel inputPanel, primPanel, kruskalPanel;
    public JPanel buttonPanel;
    public int vertexCount = 1;

    public JLabel comboLabel1, comboLabel2, textLabel3;
    public JComboBox startVertex, endVertex;
    public JTextField inputWeight;
    public JButton btnMakeEdge, btnMidResult, btnFinalResult;

    public ArrayList<Vertex> allVertex;
    public ArrayList<Edge> allEdge;

    GraphFrame(String title){
        super(title);
        inputPanel = new DrawPanel("INPUT", "#FFFFFF");
        buttonPanel = new ButtonPanel();
        primPanel = new DrawPanel("PRIM", "#81F7D8");
        kruskalPanel = new DrawPanel("KRUSKAL", "#F6CEF5");

        comboLabel1 = new JLabel("VERTEX 1");
        comboLabel2 = new JLabel("VERTEX 2");
        textLabel3 = new JLabel("가중치");
        startVertex = new JComboBox();
        endVertex = new JComboBox();
        inputWeight = new JTextField();
        btnMakeEdge = new JButton("INPUT EDGE");
        btnMidResult = new JButton("중간 결과");
        btnFinalResult = new JButton("최종 결과");

        allVertex = new ArrayList<>();
        allEdge = new ArrayList<>();

        inputPanel.setBounds(20, 50, 500, 500);
        buttonPanel.setBounds(540, 50, 200, 500);
        primPanel.setBounds(760, 50, 500, 500);
        kruskalPanel.setBounds(1280, 50, 500, 500);

        comboLabel1.setPreferredSize(new Dimension(60, 20));
        comboLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        startVertex.setPreferredSize(new Dimension(80, 20));
        comboLabel2.setPreferredSize(new Dimension(60, 20));
        comboLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        endVertex.setPreferredSize(new Dimension(80, 20));
        textLabel3.setPreferredSize(new Dimension(60, 20));
        textLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        inputWeight.setPreferredSize(new Dimension(80, 20));
        btnMakeEdge.setPreferredSize(new Dimension(120, 40));
        btnMidResult.setPreferredSize(new Dimension(120, 40));
        btnFinalResult.setPreferredSize(new Dimension(120, 40));

        inputPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int clickedX = e.getX();
                int clickedY = e.getY();
                int x_start = clickedX - 15;
                int y_start = clickedY - 15;

                if ((5 >= x_start | x_start >= 465) | (5 >= y_start | y_start >= 465)) {
                    JOptionPane.showMessageDialog(null, "정점을 그릴 수 있는 범위가 아닙니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else if (vertexCount > MAX_VERTEX) {
                    JOptionPane.showMessageDialog(null, "정점의 개수는 최대 10개입니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else {
                    Vertex vertex = new Vertex(vertexCount, clickedX, clickedY);
                    if (checkVertexOverlap(vertex)) {
                        JOptionPane.showMessageDialog(null, "정점이 겹칩니다!", "정점 생성 불가", JOptionPane.ERROR_MESSAGE);
                    } else {
                        allVertex.add(vertex);
                        startVertex.addItem(vertexCount);
                        endVertex.addItem(vertexCount);
                        inputPanel.drawVertex(vertex);
                        vertexCount += 1;
                    }
                }
            }
        });

        btnMakeEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int startVertexIndex = (int)startVertex.getSelectedItem() - 1;
                int endVertexIndex = (int)endVertex.getSelectedItem() - 1;
                Vertex start = allVertex.get(startVertexIndex);
                Vertex end = allVertex.get(endVertexIndex);

                if (startVertexIndex == endVertexIndex) {
                    JOptionPane.showMessageDialog(null, "자기 자신 연결 불가!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else if (inputWeight.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "가중치를 입력하세요!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                } else {
                    int weight = Integer.parseInt(inputWeight.getText());
                    inputPanel.makeEdge(start, end, weight);
                    /*if (checkEdgeOverlap(edge)) {
                        JOptionPane.showMessageDialog(null, "이미 존재하는 엣지입니다.", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
                    } else {
                        inputPanel.drawEdge(edge);
                        inputPanel.makeEdge(start, end, weight);
                    }*/
                }
            }
        });

        btnMidResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnFinalResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        buttonPanel.add(comboLabel1);
        buttonPanel.add(startVertex);
        buttonPanel.add(comboLabel2);
        buttonPanel.add(endVertex);
        buttonPanel.add(textLabel3);
        buttonPanel.add(inputWeight);
        buttonPanel.add(btnMakeEdge);
        buttonPanel.add(btnMidResult);
        buttonPanel.add(btnFinalResult);

        add(inputPanel);
        add(buttonPanel);
        add(primPanel);
        add(kruskalPanel);

        setLayout(null);
        setFocusable(true);
    }

    public boolean checkVertexOverlap(Vertex check) {
        boolean isOverlap = false;
        for (int i = 0; i < allVertex.size(); i++) {
            Vertex temp = allVertex.get(i);
            double deltaX = Math.abs(temp.getxPoint() - check.getxPoint());
            double deltaY = Math.abs(temp.getyPoint() - check.getyPoint());
            double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            if (check.getVertexNum() == temp.getVertexNum()) {
                continue;
            } else {
                if (length < 30) {
                    isOverlap = true;
                    break;
                }
            }
        }
        return isOverlap;
    }

    public boolean checkEdgeOverlap(Edge check) {
        boolean isOverlap = false;
        int startVertexNum = check.getStart().getVertexNum();
        int targetVertexNum = check.getEnd().getVertexNum();
        for (int i = 0; i < allEdge.size(); i++) {
            Edge temp = allEdge.get(i);
            if ((startVertexNum == temp.getStart().getVertexNum() & targetVertexNum == temp.getEnd().getVertexNum()) || targetVertexNum == temp.getStart().getVertexNum() & startVertexNum == temp.getEnd().getVertexNum()) {
                isOverlap = true;
                break;
            }
        }
        return isOverlap;
    }

    class DrawPanel extends JPanel{
        DrawPanel(String title, String color) {
            super();
            this.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), title));
            this.setBackground(Color.decode(color));
            this.setOpaque(true);
            this.setOpaque(true);
        }

        public void drawVertex(Vertex vertex) {
            int xPoint = vertex.getxPoint() - 15;
            int yPoint = vertex.getyPoint() - 15;

            Graphics graphics = getGraphics();
            graphics.setColor(Color.decode("#F7BE81"));
            graphics.fillOval(xPoint, yPoint, 30, 30);
            graphics.setColor(Color.BLACK);
            graphics.drawString(Integer.toString(vertexCount), xPoint + 12, yPoint + 18);
        }

        public void makeEdge(Vertex start, Vertex end, int weight) {
            int startVertexNum = start.getVertexNum();
            int endVertexNum = end.getVertexNum();
            boolean isCollision = false;

            for (Vertex temp : allVertex) {
                if (!(temp.getVertexNum() == startVertexNum | temp.getVertexNum() == endVertexNum)) {
                    isCollision = checkCollision(start, end, temp);
                    break;
                }
            }

            if(isCollision) {
                JOptionPane.showMessageDialog(null, "겹치는 정점 존재!", "엣지 생성 불가", JOptionPane.ERROR_MESSAGE);
            } else {
                drawEdge(start, end, weight);
            }
        }

        public boolean checkCollision(Vertex start, Vertex end, Vertex checkVertex) {
            int startX = start.getxPoint();
            int startY = start.getyPoint();
            int endX = end.getxPoint();
            int endY = end.getyPoint();

            double a, b, c;         //ax + by + c = 0
            if (startX == endX) {
                a = 1;
                b = 0;
                c = -startX;
            } else {
                a = (double)(endY - startY) / (double)(endX - startX);
                b = -1;
                c = -startX * a + startY;
            }
            double distance = (Math.abs(a * checkVertex.getxPoint() + b * checkVertex.getyPoint() + c)) / (Math.sqrt(a * a + b * b));
            if (distance <= 15) {
                if (((checkVertex.getxPoint() - 30 < startX & checkVertex.getxPoint() - 30 < endX) | (checkVertex.getxPoint() + 30 > startX & checkVertex.getxPoint() + 30 > endX)) & ((checkVertex.getyPoint() - 30 < startY & checkVertex.getyPoint() - 30 < endY) | (checkVertex.getyPoint() + 30 > startY & checkVertex.getyPoint() + 30 > endY))) {
                    return false;
                } else {
                    return true;
                }
            } else return false;
        }

        public void drawEdge(Vertex start, Vertex end, int weight) {
            int startX = start.getxPoint();
            int startY = start.getyPoint();
            int endX = end.getxPoint();
            int endY = end.getyPoint();
            int deltaX = Math.abs(endX - startX);
            int deltaY = Math.abs(endY - startY);
            double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            int xlen = (int) ((15 * deltaX) / length);
            int ylen = (int) ((15 * deltaY) / length);

            allEdge.add(new Edge(start, end, weight));
            Graphics graphics = getGraphics();
            graphics.setColor(Color.BLACK);
            if (endX > startX) {
                if (endY < startY) {
                    graphics.drawLine(startX + xlen, startY - ylen, endX - xlen, endY + ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) - 10, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX + 15, startY, endX - 15, endY);
                    drawWeight(weight, startX + (int)(0.4 * deltaX), startY + 10);
                } else {
                    graphics.drawLine(startX + xlen, startY + ylen, endX - xlen, endY - ylen);
                    drawWeight(weight, startX + (int)(0.4 * deltaX) + 5, startY + (int)(0.4 * deltaY));
                }
            } else if (endX == startX) {
                if (endY < startY) {
                    graphics.drawLine(startX, startY - 15, endX, endY + 15);
                    drawWeight(weight, startX + 5, startY - (int)(0.4 * deltaY));
                } else {
                    graphics.drawLine(startX, startY + 15, endX, endY - 15);
                    drawWeight(weight, startX + 5, startY + (int)(0.4 * deltaY));
                }
            } else {
                if (endY < startY) {
                    graphics.drawLine(startX - xlen, startY - ylen, endX + xlen, endY + ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 5, startY - (int)(0.4 * deltaY));
                } else if (endY == startY) {
                    graphics.drawLine(startX - 15, startY, endX + 15, endY);
                    drawWeight(weight, startX - (int)(0.4 * deltaX), startY - 5);
                } else {
                    graphics.drawLine(startX - xlen, startY + ylen, endX + xlen, endY - ylen);
                    drawWeight(weight, startX - (int)(0.4 * deltaX) + 10, startY + (int)(0.4 * deltaY));
                }
            }
        }

        public void drawWeight(int weight, int x, int y) {
            String Weight = Integer.toString(weight);
            Graphics graphics = getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.drawString(Weight, x, y);
        }

    }

    class ButtonPanel extends JPanel {
        ButtonPanel() {
            super();
            this.setBorder(new LineBorder(Color.RED, 1));
            this.setBackground(Color.LIGHT_GRAY);
            this.setOpaque(true);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new GraphFrame("2016010887이수호");
        frame.setSize(1810, 630);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

// TODO: 2020-11-29 프림 알고리즘 구현, 크루스칼 알고리즘 구현
