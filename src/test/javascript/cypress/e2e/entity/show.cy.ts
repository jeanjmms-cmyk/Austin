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

describe('Show e2e test', () => {
  const showPageUrl = '/show';
  const showPageUrlPattern = new RegExp('/show(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const showSample = {
    local: 'nifty furthermore',
    dataShow: '2026-05-16',
    horarioInicio: '2026-05-16T13:31:42.741Z',
    status: 'CONFIRMADO',
  };

  let show;
  let cantor;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cantors',
      body: {
        nome: 'whether oof beneath',
        generoMusical: 'defiantly coliseum grim',
        bio: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        fotoPerfil: 'aboard',
        ativo: false,
      },
    }).then(({ body }) => {
      cantor = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shows+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shows').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shows/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cantors', {
      statusCode: 200,
      body: [cantor],
    });
  });

  afterEach(() => {
    if (show) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shows/${show.id}`,
      }).then(() => {
        show = undefined;
      });
    }
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

  it('Shows menu should load Shows page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('show');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Show').should('exist');
    cy.url().should('match', showPageUrlPattern);
  });

  describe('Show page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(showPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Show page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/show/new$'));
        cy.getEntityCreateUpdateHeading('Show');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', showPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shows',
          body: {
            ...showSample,
            cantor,
          },
        }).then(({ body }) => {
          show = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shows+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shows?page=0&size=20>; rel="last",<http://localhost/api/shows?page=0&size=20>; rel="first"',
              },
              body: [show],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(showPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Show page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('show');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', showPageUrlPattern);
      });

      it('edit button click should load edit Show page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Show');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', showPageUrlPattern);
      });

      it('edit button click should load edit Show page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Show');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', showPageUrlPattern);
      });

      it('last delete button click should delete instance of Show', () => {
        cy.intercept('GET', '/api/shows/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('show').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', showPageUrlPattern);

        show = undefined;
      });
    });
  });

  describe('new Show page', () => {
    beforeEach(() => {
      cy.visit(`${showPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Show');
    });

    it('should create an instance of Show', () => {
      cy.get(`[data-cy="local"]`).type('less aged ugh');
      cy.get(`[data-cy="local"]`).should('have.value', 'less aged ugh');

      cy.get(`[data-cy="dataShow"]`).type('2026-05-15');
      cy.get(`[data-cy="dataShow"]`).blur();
      cy.get(`[data-cy="dataShow"]`).should('have.value', '2026-05-15');

      cy.get(`[data-cy="horarioInicio"]`).type('2026-05-16T09:34');
      cy.get(`[data-cy="horarioInicio"]`).blur();
      cy.get(`[data-cy="horarioInicio"]`).should('have.value', '2026-05-16T09:34');

      cy.get(`[data-cy="observacoes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="observacoes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="status"]`).select('AGENDADO');

      cy.get(`[data-cy="cantor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        show = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', showPageUrlPattern);
    });
  });
});
