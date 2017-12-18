package crats.mvcbaseproject.controller;

/**
 * Created by arsh on 2017-12-17.
 */

public interface ICharacterController {
    void fetchCharacterSuccess();
    void fetchCharacterFailure(String errorMessage);
}
