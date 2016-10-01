package com.teacherimpact.teacherimpact.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.teacherimpact.teacherimpact.ListAdapters.AboutListAdapter;
import com.teacherimpact.teacherimpact.R;
import java.util.ArrayList;

public class ActivityAbout extends AppCompatActivity {
    private ListView list;
    private AboutListAdapter aboutAdapter;
    private String[] aboutUsContentHeaders;
    private String[] aboutUsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AboutInfoUtils.initializeSections();
        aboutUsContentHeaders = AboutInfoUtils.getSectionTitles(AboutInfoUtils.aboutUsSections);
        aboutUsContent = AboutInfoUtils.getSectionContent(AboutInfoUtils.aboutUsSections);

        aboutAdapter = new AboutListAdapter(ActivityAbout.this, aboutUsContentHeaders, aboutUsContent);
        list = (ListView) findViewById(R.id.about_listview);
        if (list != null)
            list.setAdapter(aboutAdapter);

        TextView header = (TextView) findViewById(R.id.tv_about_header);
        if (header != null)
            header.setText(AboutInfoUtils.ABOUT_US_HEADER);

        setClassButtonListeners();
        setImageViewListener();
    }

    public void setClassButtonListeners() {

        Button aboutUsButton, teachersButton, studentParentButton;

        aboutUsButton = (Button) findViewById(R.id.button_aboutUs);
        if (aboutUsButton != null)
            aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.setAdapter(null);
                aboutUsContentHeaders = AboutInfoUtils.getSectionTitles(AboutInfoUtils.aboutUsSections);
                aboutUsContent = AboutInfoUtils.getSectionContent(AboutInfoUtils.aboutUsSections);
                aboutAdapter = new AboutListAdapter(ActivityAbout.this, aboutUsContentHeaders, aboutUsContent);
                list.setAdapter(aboutAdapter);

                TextView header = (TextView) findViewById(R.id.tv_about_header);
                if (header != null)
                    header.setText(AboutInfoUtils.ABOUT_US_HEADER);
            }
        });

        teachersButton = (Button) findViewById(R.id.button_teachers);
        if (teachersButton != null)
            teachersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.setAdapter(null);
                aboutUsContentHeaders = AboutInfoUtils.getSectionTitles(AboutInfoUtils.teacherSections);
                aboutUsContent = AboutInfoUtils.getSectionContent(AboutInfoUtils.teacherSections);
                aboutAdapter = new AboutListAdapter(ActivityAbout.this, aboutUsContentHeaders, aboutUsContent);
                list.setAdapter(aboutAdapter);

                TextView header = (TextView) findViewById(R.id.tv_about_header);
                if (header != null)
                    header.setText(AboutInfoUtils.TEACHER_HEADER);
            }
        });

        studentParentButton = (Button) findViewById(R.id.button_parentStudent);
        if (studentParentButton !=  null)
            studentParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.setAdapter(null);
                aboutUsContentHeaders = AboutInfoUtils.getSectionTitles(AboutInfoUtils.studentParentSections);
                aboutUsContent = AboutInfoUtils.getSectionContent(AboutInfoUtils.studentParentSections);
                aboutAdapter = new AboutListAdapter(ActivityAbout.this, aboutUsContentHeaders, aboutUsContent);
                list.setAdapter(aboutAdapter);

                TextView header = (TextView) findViewById(R.id.tv_about_header);
                if (header != null)
                    header.setText(AboutInfoUtils.PARENTS_STUDENTS_HEADER);
            }
        });
    }

    public void setImageViewListener() {
        ImageView logo;
        logo = (ImageView) findViewById(R.id.about_image);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAbout.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private static class AboutInfoUtils {
        public static final String ABOUT_US_HEADER = "5 THINGS TO KNOW ABOUT TeacherIMPACT";
        public static final String PARENTS_STUDENTS_HEADER = "Parents & Students";
        public static final String TEACHER_HEADER = "Teachers";
        public static ArrayList<Section> aboutUsSections;
        public static ArrayList<Section> teacherSections;
        public static ArrayList<Section> studentParentSections;

        public static void initializeSections() {

            aboutUsSections = new ArrayList<>();
            aboutUsSections.add(new Section(1, "1.  We're No. 1 for teachers", "Teachers matter. TeacherIMPACT is a network of verified teachers that enables teachers to showcase their professional skills to foster awareness, build parent engagement, connect with colleagues, and access awesome professional opportunities."));
            aboutUsSections.add(new Section(2, "2.  Developing the teaching profession is our focus", "Teaching is a technical skill. We believe that when parents and students understand the technical instructional decisions that each teacher makes each class, students benefit. Our vision is a future where the practice of teaching is recognized as a profession of technical skills."));
            aboutUsSections.add(new Section(3, "3.  Teacher effectiveness ratings are not accurate", "Human learning is too complex and diverse to render a value judgement on specific instructional strengths or behaviors. Effective teaching is more than student academic outcomes, and current teacher evaluation techniques cannot sufficiently capture the effectiveness of teachers."));
            aboutUsSections.add(new Section(4, "4.  We’re parents & teachers", "As parents ourselves we want to know more about children’s teachers to inform our communication and understanding of our students’ learning. We are also teachers, and we want a platform to display our professional credentials and skills to develop our individual professional careers."));
            aboutUsSections.add(new Section(5, "5.  We’re just getting started", "TeacherIMPACT is launching in Michigan and scaling from there. In fall 2015 we launched our first profile and we’ll be scaling and launching our mobile platform in spring 2016. We’ve received funding from Michigan State University and Grand Valley State University, and we’re committed to building the profession of teaching with real information about the profession from real teachers. TeacherIMPACT is made up of students, parents, teachers, developers, startup founders, school principals, philanthropists, and superintendents. Learn about opportunities to join our team."));

            teacherSections  = new ArrayList<>();
            teacherSections.add(new Section(1, "1. Why not just use LinkedIn or Facebook?", "Being a teacher isn’t a normal job and requires a unique platform that recognizes it. Security is important to school districts, and district policies often prohibit or discourage social media usage. However, school districts use TeacherIMPACT as an online marketing strategy because of its security features and unique alignment to the teaching profession. TeacherIMPACT profiles never allow for a negative or biased parent or student comment. Comments are only approved by the owner of the teacher profile and instructional skills are only endorsable for teacher selected skills."));
            teacherSections.add(new Section(2, "2. Rampant teacher choice is not a good idea.", "We agree. Most districts do not even honor teacher requests, and we should not expect that to change (and it probably shouldn’t…). However, we should expect student placement decisions to be intentional and transparent. Educators are professionals that have protocols for designing student classroom rosters to provide for the best learning environment for all students. Shielding information about the teaching practice, however, will not further the practice of teaching and the public’s understanding of it."));
            teacherSections.add(new Section(3, "3. What’s in it for me?", "Teachers use a TeacherIMPACT profile for many reasons like building a strong platform parent and student dialogue about instruction, showcasing professional credentials, and accessing awesome opportunities like tutoring, freelance consulting, product evaluation, speaking engagements, expert testimony, and peer to peer learning connections."));
            teacherSections.add(new Section(4, "4. Is this a popularity contest?", "No. Parent and Students users can only endorse an instructional skill once and their endorsement is public."));
            teacherSections.add(new Section(5, "5. Can my profile be stolen?", "No. TeacherIMPACT profiles are authenticated by a school email assigned to each specific teacher."));
            teacherSections.add(new Section(6, "6. Can my profile be used by my supervisor in my evaluation?", "Perhaps. We suppose a supervisor could elect to use it within an evaluation. Most teacher evaluation frameworks evaluate specific instructional techniques, not public information from the web. Since TeacherIMPACT profiles are completely controlled by the teacher, if a profile was used a component to an evaluation, it would most likely benefit the teacher."));
            teacherSections.add(new Section(7, "7. How can I get a TeacherIMPACT profile?", "Once your school is registered with TeacherIMPACT, we’ll email you an invitation to claim your profile. If your school is not yet registered with TeacherIMPACT, contact us to register your school."));
            teacherSections.add(new Section(8, "8. How long does it take to complete a profile?", "It takes about 10-20 minutes."));
            teacherSections.add(new Section(9, "9. How often should I update or check my profile?", "Update your profile as your professional skills change. If you attend a professional conference or obtain a new credential, you should update your profile.  You should also check your profile regularly to approve student and parent comments that you may decide to add to your profile. In future releases, your TeacherIMPACT profile will provide you with current trends in your specialty and geographic area, opportunities for your specific skills and interests, and career opportunities."));
            teacherSections.add(new Section(10, "10. How does security work?", "TeacherIMPACT profiles can only be claimed by teachers currently teaching at a TeacherIMPACT approved school. Only rostered teachers listed on the school’s website can authenticate a TeacherIMPACT profile. Once the teacher has authenticated profile ownership, the teacher can change the username to their private email address."));

            studentParentSections  = new ArrayList<>();
            studentParentSections.add(new Section(1, "1. What's the quick overview?", "Information on teacher’s backgrounds, credentials, and skills is hard to find yet incredibly useful for communicating with teachers and understanding the specific instructional strengths of teachers.  TeacherIMPACT profiles provides this information free to the public. Profile information is used by parents and students to enhance communication, build class schedules, and learn about the teaching practice."));
            studentParentSections.add(new Section(2, "2. How can I endorse or leave a comment about a teacher?", "Create a Parent or Student username. Anyone can create a unique username and password for a Parent or Student profile. Both profiles allow users to endorse teachers and leave comments. Endorsements and comments are not available to public visitors."));
            studentParentSections.add(new Section(3, "3. Why should I create a user name?", "Parents and Students that create a username have immediate access to the CourseBuilder function that allows Parents and Students to create a roster of favorite teachers for future courses; additionally, registered Parents and Students can follow the updates and teaching assignments of specific teachers. Most valuable, Parent and Student registered users profiles have access to interest and geographic specific information about schools, teachers, and education in your region, not just in your school."));
            studentParentSections.add(new Section(4, "4. How can I advocate for a specific teacher?", "Schools have different policies about accommodating teacher requests. TeacherIMPACT profiles can capture only the surface of a teacher’s expertise, and a complete understanding is the responsibility of the school employing the teacher. Principals, superintendents, and building leaders design classroom rosters to best match the unique talents of their teachers with the unique learning needs of their students."));
            studentParentSections.add(new Section(5, "5. I would like to connect with another parent or student to discuss a teacher - Can I do that?", "Good idea. Teaching and learning is important and fun to discuss and important to us too. To connect with a Parent or Student user that endorsed or commented on a teacher profile, just click on their name and the messaging function will enable you to privately message that Parent or Student."));
            studentParentSections.add(new Section(5, "6. How does privacy work?", "Parent and Student user accounts are private. Only usernames are publicly visible. Direct messaging occurs within TeacherIMPACT’s private message client."));
        }

        public static String[] getSectionTitles(ArrayList<Section> list) {
            String[] result = new String[list.size()];
            int i = 0;
            for (Section s : list) {
                result[i] = s.sectionTitle;
                i++;
            }
            return result;
        }

        public static String[] getSectionContent(ArrayList<Section> list) {
            String[] result = new String[list.size()];
            int i = 0;
            for (Section s : list) {
                result[i] = s.sectionContent;
                i++;
            }
            return result;
        }

        static class Section {
            public int section;
            public String sectionTitle;
            public String sectionContent;

            public Section(int section, String sectionTitle, String sectionContent) {
                this.section = section;
                this.sectionTitle = sectionTitle;
                this.sectionContent = sectionContent;
            }
        }
    }
}




