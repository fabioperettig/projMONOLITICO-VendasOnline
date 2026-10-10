package com.fabioperettig.domain;

public interface Persistence {

    /**
     * A interface com getId garante que toda classe de Entidade que a implemente
     * ofereça este metodo e permite que o DAO genérico o utilize sem conhecer
     * a entidade específica.
     * @return
     */
    Long getId();
}
