import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import javax.swing.border.*;

import javax.swing.*;

public class TimeAlgorism extends JFrame {
	Subject[][] subjectList = new Subject[10][6];
	int scheduleListCount = 0; // ��ü �ð�ǥ ���� ���
	
	private class Subject {
		String name;
		String professor;
		int day1, day2;
		int time1, time2;
		int totalnum; // ���� �ο�
		int permittednum; // ��������
		float subCompetition; // ����� �����
	}
	
	private class ScheduleList {
		String name;
		String[][] schedule = new String[8][5]; // [����][����]
		float totalCompetition = 1;
	}
	
	private ScheduleList storeInSchedule(Subject thissub, ScheduleList temp) {
		int day1 = thissub.day1; 
		int time1 = thissub.time1; 
		int day2 = thissub.day2; 
		int time2 = thissub.time2; 
		temp.schedule[time1][day1] = thissub.name +"\n"+ thissub.professor;
		temp.schedule[time2][day2] = thissub.name +"\n"+ thissub.professor;
		temp.totalCompetition *= thissub.subCompetition;
		return temp;
	}
	
	private ScheduleList deleteSubInIdx(Subject thissub, ScheduleList temp){
		int curDay1 = thissub.day1; 
		int curTime1 = thissub.time1; 
		int curDay2 = thissub.day2; 
		int curTime2 = thissub.time2; 
		temp.schedule[curTime1][curDay1] = null;
		temp.schedule[curTime2][curDay2] = null;
		return temp;
	}
	private void run() {
		
		Map<String, Integer> dayToInt = new HashMap<>();
		dayToInt.put("��", 0);
		dayToInt.put("ȭ", 1);
		dayToInt.put("��", 2);
		dayToInt.put("��", 3);
		dayToInt.put("��", 4);
		dayToInt.put("������", 0);
		dayToInt.put("ȭ����", 1);
		dayToInt.put("������", 2);
		dayToInt.put("�����", 3);
		dayToInt.put("�ݿ���", 4);
		
		Scanner sc = new Scanner(System.in);
		int subjectCount = 0;
		int [] professorCount = new int[5];

		//System.out.print("�� �⵵ �� �б��ΰ���?");
		//String semester = sc.next();

		System.out.print("������� �Է��ϼ���>> ");
		String subjectn = sc.next();
		for (int i = 0;; i++) {
			for (int j = 0;; j++) {			
				subjectList[i][j] = new Subject();
				subjectList[i][j].name = subjectn;
				
				System.out.print(subjectList[i][j].name+" ������ �������� �Է��ϼ���. �׸��Ϸ��� q�� �Է��ϼ���>> ");
				String next = sc.next();
				if (next.equals("q"))
					break;
				subjectList[i][j].professor = next;
				
				System.out.print(subjectList[i][j].name+" ù ��° �ð��� ������ �Է��ϼ���>> ");
				subjectList[i][j].day1 = (int) dayToInt.get(sc.next());
				// ������ ���ڷ� ����
				
				System.out.print("ù ��° �ð��� ���ø� �Է��ϼ���(ex) 1)>> ");
				subjectList[i][j].time1 = sc.nextInt() - 1;
				
				System.out.print(subjectList[i][j].name+"�� �� ��° �ð��� ������ �Է��ϼ���>> ");
				subjectList[i][j].day2 = (int) dayToInt.get(sc.next());
				
				System.out.print("�� ��° �ð��� ���ø� �Է��ϼ���(ex) 1)>> ");
				subjectList[i][j].time2 = sc.nextInt() - 1;

				System.out.print(subjectList[i][j].name+"�� ���������� �Է��ϼ���>> ");
				subjectList[i][j].permittednum = sc.nextInt();
				System.out.print(subjectList[i][j].name+"�� �� ���� �ο��� �Է��ϼ���>> ");
				subjectList[i][j].totalnum = sc.nextInt();
				if(subjectList[i][j].permittednum > subjectList[i][j].totalnum)
					subjectList[i][j].subCompetition = 1;
				else 
					subjectList[i][j].subCompetition = subjectList[i][j].permittednum / (float)subjectList[i][j].totalnum;
				professorCount[i]++; 
			}
			System.out.print("������� �Է��ϼ���. �׸��Ϸ��� q�� �Է��ϼ���>>");
			subjectn = sc.next();
			subjectCount++;
			if (subjectn.equals("q"))
				break;
		}
		
		// ���� �Ϸ�
		ScheduleList list[] = new ScheduleList[100]; // ������ �ð�ǥ���� ������ �迭
		int[] stored = new int[subjectCount]; // ������ ����� �����ε����� ���� stored[����] = ����
		int storedIdx = -1;
		loop : while(true) {
		ScheduleList temp = new ScheduleList(); // ���ο� ����Ʈ ����
		for (int i = 0; i < subjectCount; i++) { // �� ���� �������� ������ ����
			  if (i < 0) break loop;
			  
			  boolean containsSubject = false;
			  int n = 0;
			  if (storedIdx != -1) n = storedIdx;
			  for (int j = n; j < professorCount[i]; j++) {
			    storedIdx = -1;
			    Subject thissub = subjectList[i][j];
			    int day1 = thissub.day1; // ����
			    int time1 = thissub.time1; // �ð�
			    int day2 = thissub.day2; // ����
			    int time2 = thissub.time2; // �ð�

			    if (temp.schedule[time1][day1] == null && temp.schedule[time2][day2] == null) { // ��������� ����. ����Ǿ��� �� ����Ǵ� �ڵ�
			      temp = storeInSchedule(thissub, temp);
			      containsSubject = true;
			      stored[i] = j; // ������ ����� �����ε���
			      break;
			    }
			  }
			  if (!containsSubject) {
			    int before = i - 1; // ���� ���� �ε���
			    if (before < 0) break loop; // ���� �������� ���ư� �� ���� -> ����Ʈ ���� ����

			    // ���� ������ ���� �ݺ����� �Ѿ.	
			    while (true) {
			      if (i < 0) break loop;

			      Subject thissub = subjectList[before][stored[before]];
			      // ���� ������ ��� ����.
			      temp = deleteSubInIdx(thissub, temp);

			      //j�� �������� �������� �Ѿ�� �� ������������ �Ѿ
			      if (stored[before] + 1 <= professorCount[before] - 1) { // ������ ����� �ε��� ���� �ε����� �����ϸ�
			        i -= 2; // i - 1�ϸ� ���� �ݺ��� �Ȱ���, i - 2 �ϸ� ���� �ݺ��� ����
			        storedIdx = stored[before] + 1; // ������ �����ε������� ����
			        before--;
			        break;
			      }
			    }
			  }
			  if (i == subjectCount - 1 && containsSubject) { // ������ ������� ����Ϸ�!
			    list[scheduleListCount++] = temp;
			    temp = new ScheduleList();
			    for (int k = 0; k < subjectCount - 1; k++) { // ���� �ε��������� �Ȱ��� �����ϰ� �����ͺ��� �ٽ� Ž��
			      Subject thissub = subjectList[k][stored[k]];
			      temp = storeInSchedule(thissub, temp);
			    }

			    while (true) {
			      // ���� ������ ��� ����.
			      Subject thissub = subjectList[i][stored[i]];
			      temp = deleteSubInIdx(thissub, temp);

			      if (stored[i] + 1 <= professorCount[i] - 1) { // ����� �ε��� ���� �ε����� �����ϸ�
			        storedIdx = stored[i] + 1;
			        i--; // �ٽ� ���� �ݺ�
			        break;
			      }
			      i--;
			      if (i < 0) {
			        break loop;
			      }
			    }
			  }
			}
		}
		toString(list);
		drawGUI(list);
		sc.close();
	}
	public static void main(String[] args) {
		TimeAlgorism TA = new TimeAlgorism();
		TA.run();
	}

void toString(ScheduleList[] list) {
	for(int k = 0; k < scheduleListCount; k++) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 5; j++) {
				if(list[k].schedule[i][j] == null) System.out.print("--");
				else System.out.print(list[k].schedule[i][j]);
			}
			System.out.println();
		}
		System.out.println("------------------------------------------");
	}
}

void drawGUI(ScheduleList[] list) {
	JFrame scheduleList = new JFrame();
	scheduleList.setBackground(Color.BLACK);
	scheduleList.getContentPane().setBackground(Color.BLACK);
	scheduleList.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); //�¿찣��, ���ϰ���
	for (int k = 0; k < scheduleListCount; k++) {
		JPanel scheduleBoard = new JPanel();
		scheduleBoard.setBackground(Color.white);
		scheduleBoard.setLayout(new BorderLayout(10, 5));
		
		JLabel percentage = new JLabel("���� Ȯ��: "+String.format("%.1f", list[k].totalCompetition * 100)+"%");
		percentage.setHorizontalAlignment(JLabel.CENTER);
		percentage.setFont(new Font("@AppleSDGothicNeoM00", Font.BOLD, 16));
		JPanel schedule = new JPanel();
		schedule.setBorder(new BevelBorder(BevelBorder.RAISED));
		schedule.setSize(60,90);
		schedule.setLayout(new GridLayout(9, 6));
		
		JLabel[][] label = new JLabel[9][6];
		
		scheduleBoard.add(schedule, BorderLayout.CENTER);
		scheduleBoard.add(percentage, BorderLayout.SOUTH);
		scheduleList.add(scheduleBoard);
		
		for(int m = 0; m < 9; m++) {
			   for(int n = 0; n < 6; n++) {
				  label[m][n] = new JLabel();
			      schedule.add(label[m][n]);
			      schedule.setBackground(Color.WHITE); // ǥ ����
			      label[m][n].setAlignmentX(CENTER_ALIGNMENT);
			      label[m][n].setBorder(new EtchedBorder(EtchedBorder.RAISED));
			      label[m][n].setFont(new Font("�������", Font.PLAIN, 16));
			      // schedule.setFont(new Font("@AppleSDGothicNeoM00", Font.PLAIN, 16));
			   }
		}
		label[0][1].setText("��");
		label[0][2].setText("ȭ");
		label[0][3].setText("��");
		label[0][4].setText("��");
		label[0][5].setText("��");

		for(int i = 0; i < 8; i++) {
			label[i+1][0].setText(" "+(i+1)+"���� ");
			for(int j = 0; j < 5; j++) {
				if(list[k].schedule[i][j] != null)
					label[i+1][j+1].setText(convertToMultiline(list[k].schedule[i][j])); // �����/������ �и��Ͽ� ���
					//label[i+1][j+1].setForeground(Color.ORANGE);
		        }
		}
	}
	scheduleList.setTitle("�ð�ǥ ������");
    scheduleList.setSize(1000, 750);
    scheduleList.setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
public String convertToMultiline(String str)
{
    return "<html>" + str.replaceAll("\n", "<br>");
}
}

