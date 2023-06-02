/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author manel
 */
public class Validadors {
    
    public static List<String> validaBean(Object b)
    {
        List<String> ret = new ArrayList<>();
        
        Configuration<?> config = Validation.byDefaultProvider().configure();
        
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
            
        validator.validate(b).stream().forEach(x -> ret.add(x.getMessage()));
        
        return ret;
    }
    
}
