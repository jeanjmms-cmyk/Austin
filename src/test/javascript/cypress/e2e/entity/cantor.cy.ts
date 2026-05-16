import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Cantor e2e test', () => {
  const cantorPageUrl = '/cantor';
  const cantorPageUrlPattern = new RegExp('/cantor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cantorSample = { nome: 'why silt and', ativo: false };

  let cantor;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cantors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cantors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cantors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cantor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cantors/${cantor.id}`,
      }).then(() => {
        cantor = undefined;
      });
    }
  });

  it('Cantors menu should load Cantors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cantor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cantor').should('exist');
    cy.url().should('match', cantorPageUrlPattern);
  });

  describe('Cantor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cantorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cantor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cantor/new$'));
        cy.getEntityCreateUpdateHeading('Cantor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cantorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cantors',
          body: cantorSample,
        }).then(({ body }) => {
          cantor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cantors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cantors?page=0&size=20>; rel="last",<http://localhost/api/cantors?page=0&size=20>; rel="first"',
              },
              body: [cantor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cantorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cantor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cantor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cantorPageUrlPattern);
      });

      it('edit button click should load edit Cantor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cantor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cantorPageUrlPattern);
      });

      it('edit button click should load edit Cantor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cantor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cantorPageUrlPattern);
      });

      it('last delete button click should delete instance of Cantor', () => {
        cy.intercept('GET', '/api/cantors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cantor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cantorPageUrlPattern);

        cantor = undefined;
      });
    });
  });

  describe('new Cantor page', () => {
    beforeEach(() => {
      cy.visit(`${cantorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cantor');
    });

    it('should create an instance of Cantor', () => {
      cy.get(`[data-cy="nome"]`).type('between');
      cy.get(`[data-cy="nome"]`).should('have.value', 'between');

      cy.get(`[data-cy="generoMusical"]`).type('whether up');
      cy.get(`[data-cy="generoMusical"]`).should('have.value', 'whether up');

      cy.get(`[data-cy="bio"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="bio"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="fotoPerfil"]`).type('etch onto in');
      cy.get(`[data-cy="fotoPerfil"]`).should('have.value', 'etch onto in');

      cy.get(`[data-cy="ativo"]`).should('not.be.checked');
      cy.get(`[data-cy="ativo"]`).click();
      cy.get(`[data-cy="ativo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cantor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', cantorPageUrlPattern);
    });
  });
});
