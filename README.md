![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![WildFly](https://img.shields.io/badge/WildFly-%23f0473e.svg?style=for-the-badge&logo=redhat&logoColor=white)
![Curso EBAC](https://img.shields.io/badge/Curso--EBAC-f2f0ef?style=for-the-badge)

# 📚☕️ Projeto Monolítico para Web – AchievementsDAO 🏅

Primeiro projeto monolítico para Web com persistência PostgreSQL, implementação Wildfly e interação frontend em
JSF(Java Server Faces). O projeto consiste em cadastro de usuários e registros de Achievements que o usuário conquistou.
A estrutura é composta de duas `Entidades` simples "User" e "Achievements" com anotação JPA `@ManyToOne` (um usuário
com vários achievements).

| Classe        | Descrição                                                 |
|---------------|-----------------------------------------------------------|
| `User`        | Entidade central persistida @OneToMany                    |
| `Achievement` | Entidade persistida com dependência @ManyToOne de usuário |

<details><summary>Estrutura do projeto</summary>

````
main
└── java
    ├── com.fabioperettig
    |   ├── controller
    |   |   ├── UserController
    |   |   └── AchievementController
    |   ├── dao
    |   |   ├── generic
    |   |   |   ├── GenericDAO
    |   |   |   └── IGenericDAO
    |   |   ├── AchievementDAO
    |   |   └── UserDAO
    |   ├── domain
    |   |   ├── Achievement
    |   |   ├── Persistence
    |   |   └── User
    |   └── service
    |       ├── AchievementService
    |       └── UserService
    ├── resources
    |   └── META-INF
    |       └── persistence.xml
    └── webapp
        ├── WEB-INF
        |   └── web.xml
        └── users.xhtml    
````
</details>

## Docker 🐳

Tanto o `PostgreSQL` quanto o `Wildfly` foram contruidos em containers de [compose.yaml](compose.yaml), com dados
implementados em variáveis de ambiente com `.env`.

```dockerfile
services:
  postgres:
    image: postgres:15

    environment:
      POSTGRES_DB: ${POSTGRES_DB:?Defina POSTGRES_DB no .env}
      POSTGRES_USER: ${POSTGRES_USER:?Defina POSTGRES_USER no .env}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:?Defina POSTGRES_PASSWORD no .env}

    ports:
      - "127.0.0.1:${POSTGRES_PORT:-5434}:5432"

    volumes:
      - postgres_data:/var/lib/postgresql/data

    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U \"$${POSTGRES_USER}\" -d \"$${POSTGRES_DB}\""]
      interval: 5s
      timeout: 5s
      retries: 10
      start_period: 10s

  wildfly:
    build:
      context: .
      dockerfile: Dockerfile

    image: conquistas-wildfly:dev

    environment:
      DB_URL: ${DB_URL:?Defina DB_URL no .env}
      DB_USER: ${POSTGRES_USER:?Defina POSTGRES_USER no .env}
      DB_PASSWORD: ${POSTGRES_PASSWORD:?Defina POSTGRES_PASSWORD no .env}


    ports:
      - "127.0.0.1:${WILDFLY_HTTP_PORT:-8080}:8080"

    depends_on:
      postgres:
        condition: service_healthy

volumes:
  postgres_data:
```

## Entidade

As entidades foram implementadas em ***Java Persistence API***, implementando contrato `Persistence` para garantir o
método getId() e contrato `Serializable` para o uso de aplicação Web Wildfly.

<details><summary>Entidade Cliente</summary>

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER")
public class User implements Persistence, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "u.seq")
    @SequenceGenerator(name = "u.seq", sequenceName = "sequence_user", initialValue = 1, allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "USUARIO", nullable = false, unique = true)
    private String apelido;

    @Column(name = "NIVEL", nullable = false)
    private Integer nivel = 1;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Achievement> achievements = new ArrayList<>();
}
```

</details>

## DAO com Windfly 

Agora com o uso do WildFly o gerenciamento da camada DAO sofrerá mudanças em comparação com JPA convencional. O Wildfly
assume o controle do **ciclo de vida do JPA** de forma automática, não sendo mais necessário o uso explícito de `begin()`,
`commit()` e nem de `close()` do EntityManager.

Essa automatização ocorre atráves da anotação `@PersistenceContext` que recebe a *unitName* e faz a injeção de dependência
do EntityManagerFactory.

```java
import com.fabioperettig.domain.Persistence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public abstract class GenericDAO<T extends Persistence> implements IGenericDAO<T> {

    protected final Class<T> entityClass;

    @PersistenceContext(unitName = "ProjMONOAchievements")
    protected EntityManager entityManager;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }


    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T read(Long id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
            return;
        }

        T managedEntity = entityManager.find(entityClass, entity.getId());

        if (managedEntity != null) {
            entityManager.remove(managedEntity);
        }
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT entity FROM "
                + entityClass.getSimpleName()
                + " entity";

        return entityManager
                .createQuery(jpql, entityClass)
                .getResultList();
    }
}
```

## ------- Projeto em construção 🚧 -------

-----

## 📖 Projeto Guia

Segui o projeto disponibilizado no módulo 38 como base, contruindo a mesma estrutura e distribuição de responsabilidades
do projeto. Mas, também procurei tomar decisões diferentes que agregasse nos estudos e tornasse o projeto mais pessoal.

> Repositório oficial do projeto usado de base:
> https://github.com/digaomilleniun/backend-java-ebac/tree/main/mod38/VendasOnline