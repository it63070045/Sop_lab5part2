package com.example.lab05part2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route("index2")
public class MyView2 extends HorizontalLayout {
    TextField addword = new TextField("Add Word");
    Button addgood = new Button("Add Good Word");
    Button addevil = new Button("Add Bad Word");
    ComboBox<String> listgood = new ComboBox<>("Good Words");
    ComboBox<String> listevil = new ComboBox<>("Bad Words");
    VerticalLayout contain1 = new VerticalLayout();
    TextField addsen = new TextField("Add Sentence");
    Button butsen = new Button("Add Sentence");
    TextArea goodsen = new TextArea("Good Sentences");
    TextArea evilsen = new TextArea("Bad Sentences");
    Button showsen = new Button("Show Sentences");
    VerticalLayout contain2 = new VerticalLayout();
    public MyView2(){
        addword.setWidth("100%");
        addgood.setWidth("100%");
        addevil.setWidth("100%");
        listgood.setWidth("100%");
        listevil.setWidth("100%");
        addsen.setWidth("100%");
        butsen.setWidth("100%");
        goodsen.setWidth("100%");
        evilsen.setWidth("100%");
        showsen.setWidth("100%");
        goodsen.setEnabled(false);
        evilsen.setEnabled(false);
        contain1.add(addword, addgood, addevil, listgood, listevil);
        contain2.add(addsen, butsen, goodsen, evilsen, showsen);
        add(contain1, contain2);
        addgood.addClickListener(event -> {
            List out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGood/")
                    .body(Mono.just(addword.getValue()), String.class)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            listgood.setItems(out);
            addword.setValue("");
        });
        addevil.addClickListener(event -> {
            List addbad = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBad/")
                    .body(Mono.just(addword.getValue()), String.class)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            listevil.setItems(addbad);
            addword.setValue("");
        });
        butsen.addClickListener(event -> {
                WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proof/")
                    .body(Mono.just(addsen.getValue()), String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
                addsen.setValue("");
        });
        showsen.addClickListener(event -> {
            Sentence sen = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence/")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            goodsen.setValue(sen.goodSentences+"");
            evilsen.setValue(sen.badSentences+"");
        });
    }

}
