package codereading.profile;

import codereading.domain.AnswerOption;
import codereading.domain.Question;
import codereading.domain.QuestionSeries;
import codereading.domain.User;
import codereading.service.QuestionSeriesService;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {

    @Autowired
    private QuestionSeriesService questionSeriesService;

    @PostConstruct
    public void init() {
        User user1 = new User();


        QuestionSeries series1 = new QuestionSeries();
        series1.setTitle("Olion metodikutsut");
        Question question1 = new Question();
        question1.setCreator(user1);
        question1.setTitle("Mikä näistä koodiriveistä ei aiheuta virhettä?");
        question1.setCode("public class Opiskelija {\n\n    private int opintopisteet;\n"
                + "\n    public Opiskelija() {\n    }\n\n    public void nuku() {\n"
                + "        this.opintopisteet = this.opintopisteet + 2;\n"
                + "    }\n}\n\n-------------------------------\n\n"
                + "public static void main(String[] args) {\n"
                + "    Opiskelija mika = new Opiskelija();\n"
                + "    // Koodirivi tulee tähän\n}");
        
        AnswerOption option1 = new AnswerOption();
        option1.setAnswerText("mika.nuku()");
        option1.setIsCorrect(true);
        AnswerOption option2 = new AnswerOption();
        option2.setAnswerText("Opiskelija.nuku();");

        List<AnswerOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        question1.setAnswerOptions(options);
        
        series1.addQuestion(question1);

        questionSeriesService.save(series1);
    }
}
