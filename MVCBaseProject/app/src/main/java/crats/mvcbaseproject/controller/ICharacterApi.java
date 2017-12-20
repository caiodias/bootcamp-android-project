package crats.mvcbaseproject.controller;

import java.util.ArrayList;

import crats.mvcbaseproject.model.Character;


/**
 * Created by arsh on 2017-12-17.
 */

public interface ICharacterApi {
    void fetchSuccess(ArrayList<Character> list);
    void fetchFailure(String errorMessage);

}
