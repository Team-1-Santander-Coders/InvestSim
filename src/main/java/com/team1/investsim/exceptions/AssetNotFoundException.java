package com.team1.investsim.exceptions;

public class AssetNotFoundException extends RuntimeException{
    public AssetNotFoundException(){ super("Ação não encontrada");}
}
