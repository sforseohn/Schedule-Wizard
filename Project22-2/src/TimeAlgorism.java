import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import javax.swing.border.*;

import javax.swing.*;

public class TimeAlgorism extends JFrame {
	Subject[][] subjectList = new Subject[10][6];
	int scheduleListCount = 0; // 전체 시간표 개수 출력
	
	private class Subject {
		String name;
		String professor;
		int day1, day2;
		int time1, time2;
		int totalnum; // 담은 인원
		int permittednum; // 수강정원
		float subCompetition; // 과목당 경쟁률
	}
	
	private class ScheduleList {
		String name;
		String[][] schedule = new String[8][5]; // [교시][요일]
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
		dayToInt.put("월", 0);
		dayToInt.put("화", 1);
		dayToInt.put("수", 2);
		dayToInt.put("목", 3);
		dayToInt.put("금", 4);
		dayToInt.put("월요일", 0);
		dayToInt.put("화요일", 1);
		dayToInt.put("수요일", 2);
		dayToInt.put("목요일", 3);
		dayToInt.put("금요일", 4);
		
		Scanner sc = new Scanner(System.in);
		int subjectCount = 0;
		int [] professorCount = new int[5];

		//System.out.print("몇 년도 몇 학기인가요?");
		//String semester = sc.next();

		System.out.print("과목명을 입력하세요>> ");
		String subjectn = sc.next();
		for (int i = 0;; i++) {
			for (int j = 0;; j++) {			
				subjectList[i][j] = new Subject();
				subjectList[i][j].name = subjectn;
				
				System.out.print(subjectList[i][j].name+" 과목의 교수명을 입력하세요. 그만하려면 q를 입력하세요>> ");
				String next = sc.next();
				if (next.equals("q"))
					break;
				subjectList[i][j].professor = next;
				
				System.out.print(subjectList[i][j].name+" 첫 번째 시간의 요일을 입력하세요>> ");
				subjectList[i][j].day1 = (int) dayToInt.get(sc.next());
				// 요일을 숫자로 저장
				
				System.out.print("첫 번째 시간의 교시를 입력하세요(ex) 1)>> ");
				subjectList[i][j].time1 = sc.nextInt() - 1;
				
				System.out.print(subjectList[i][j].name+"의 두 번째 시간의 요일을 입력하세요>> ");
				subjectList[i][j].day2 = (int) dayToInt.get(sc.next());
				
				System.out.print("두 번째 시간의 교시를 입력하세요(ex) 1)>> ");
				subjectList[i][j].time2 = sc.nextInt() - 1;

				System.out.print(subjectList[i][j].name+"의 수강정원을 입력하세요>> ");
				subjectList[i][j].permittednum = sc.nextInt();
				System.out.print(subjectList[i][j].name+"의 총 담은 인원을 입력하세요>> ");
				subjectList[i][j].totalnum = sc.nextInt();
				if(subjectList[i][j].permittednum > subjectList[i][j].totalnum)
					subjectList[i][j].subCompetition = 1;
				else 
					subjectList[i][j].subCompetition = subjectList[i][j].permittednum / (float)subjectList[i][j].totalnum;
				professorCount[i]++; 
			}
			System.out.print("과목명을 입력하세요. 그만하려면 q를 입력하세요>>");
			subjectn = sc.next();
			subjectCount++;
			if (subjectn.equals("q"))
				break;
		}
		
		// 저장 완료
		ScheduleList list[] = new ScheduleList[100]; // 가능한 시간표들을 저장한 배열
		int[] stored = new int[subjectCount]; // 이전의 저장된 교수인덱스를 저장 stored[과목] = 교수
		int storedIdx = -1;
		loop : while(true) {
		ScheduleList temp = new ScheduleList(); // 새로운 리스트 생성
		for (int i = 0; i < subjectCount; i++) { // 총 과목 개수마다 과목을 담음
			  if (i < 0) break loop;
			  
			  boolean containsSubject = false;
			  int n = 0;
			  if (storedIdx != -1) n = storedIdx;
			  for (int j = n; j < professorCount[i]; j++) {
			    storedIdx = -1;
			    Subject thissub = subjectList[i][j];
			    int day1 = thissub.day1; // 요일
			    int time1 = thissub.time1; // 시간
			    int day2 = thissub.day2; // 요일
			    int time2 = thissub.time2; // 시간

			    if (temp.schedule[time1][day1] == null && temp.schedule[time2][day2] == null) { // 비어있으면 저장. 저장되었을 때 실행되는 코드
			      temp = storeInSchedule(thissub, temp);
			      containsSubject = true;
			      stored[i] = j; // 이전의 저장된 교수인덱스
			      break;
			    }
			  }
			  if (!containsSubject) {
			    int before = i - 1; // 이전 과목 인덱스
			    if (before < 0) break loop; // 이전 과목으로 돌아갈 수 없음 -> 리스트 저장 종료

			    // 이전 과목의 다음 반복으로 넘어감.	
			    while (true) {
			      if (i < 0) break loop;

			      Subject thissub = subjectList[before][stored[before]];
			      // 이전 과목의 기록 지움.
			      temp = deleteSubInIdx(thissub, temp);

			      //j가 이전과목 교수수를 넘어가면 더 이전과목으로 넘어감
			      if (stored[before] + 1 <= professorCount[before] - 1) { // 이전의 저장된 인덱스 다음 인덱스가 존재하면
			        i -= 2; // i - 1하면 다음 반복에 똑같음, i - 2 하면 다음 반복시 이전
			        storedIdx = stored[before] + 1; // 이전의 다음인덱스부터 시작
			        before--;
			        break;
			      }
			    }
			  }
			  if (i == subjectCount - 1 && containsSubject) { // 마지막 과목까지 저장완료!
			    list[scheduleListCount++] = temp;
			    temp = new ScheduleList();
			    for (int k = 0; k < subjectCount - 1; k++) { // 이전 인덱스까지는 똑같이 저장하고 다음것부터 다시 탐색
			      Subject thissub = subjectList[k][stored[k]];
			      temp = storeInSchedule(thissub, temp);
			    }

			    while (true) {
			      // 현재 과목의 기록 지움.
			      Subject thissub = subjectList[i][stored[i]];
			      temp = deleteSubInIdx(thissub, temp);

			      if (stored[i] + 1 <= professorCount[i] - 1) { // 저장된 인덱스 다음 인덱스가 존재하면
			        storedIdx = stored[i] + 1;
			        i--; // 다시 현재 반복
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
	scheduleList.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); //좌우간격, 상하간격
	for (int k = 0; k < scheduleListCount; k++) {
		JPanel scheduleBoard = new JPanel();
		scheduleBoard.setBackground(Color.white);
		scheduleBoard.setLayout(new BorderLayout(10, 5));
		
		JLabel percentage = new JLabel("성공 확률: "+String.format("%.1f", list[k].totalCompetition * 100)+"%");
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
			      schedule.setBackground(Color.WHITE); // 표 색깔
			      label[m][n].setAlignmentX(CENTER_ALIGNMENT);
			      label[m][n].setBorder(new EtchedBorder(EtchedBorder.RAISED));
			      label[m][n].setFont(new Font("나눔고딕", Font.PLAIN, 16));
			      // schedule.setFont(new Font("@AppleSDGothicNeoM00", Font.PLAIN, 16));
			   }
		}
		label[0][1].setText("월");
		label[0][2].setText("화");
		label[0][3].setText("수");
		label[0][4].setText("목");
		label[0][5].setText("금");

		for(int i = 0; i < 8; i++) {
			label[i+1][0].setText(" "+(i+1)+"교시 ");
			for(int j = 0; j < 5; j++) {
				if(list[k].schedule[i][j] != null)
					label[i+1][j+1].setText(convertToMultiline(list[k].schedule[i][j])); // 과목명/교수명 분리하여 출력
					//label[i+1][j+1].setForeground(Color.ORANGE);
		        }
		}
	}
	scheduleList.setTitle("시간표 마법사");
    scheduleList.setSize(1000, 750);
    scheduleList.setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
public String convertToMultiline(String str)
{
    return "<html>" + str.replaceAll("\n", "<br>");
}
}

