package com.example.notes.views.list.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;

// Test input field
@Tag("input")
public class InputComponent extends Component {

    public InputComponent(){
        this(null,null);
    }
    public InputComponent(String value,String placeholder){
         getElement().setProperty("value",value);
         getElement().setProperty("placeholder",value);
     }

     @Synchronize("change")
     public String getValue(){
         return getElement().getProperty("value");
     }

     @Synchronize("change")
     public String getPlaceholder(){
         return getElement().getProperty("value");
     }

     public void setValue(String value){
         getElement().setProperty("value",value);
     }

     public void setPlaceholder(String placeholder) {
         getElement().setProperty("placeholder",placeholder);
     }
}
