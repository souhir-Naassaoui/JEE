package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Client;

public class ClientDaoImpl implements ClientDao {

    private static final String SQL_SELECT        = "SELECT id, nom, prenom, adresse, telephone, email, image FROM Client ORDER BY id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, nom, prenom, adresse, telephone, email, image FROM Client WHERE id = ?";
    private static final String SQL_INSERT        = "INSERT INTO Client (nom, prenom, adresse, telephone, email, image) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM Client WHERE id = ?";

    private DAOFactory          daoFactory;

    ClientDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    @Override
    public Client trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    @Override
    public void creer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,
                    client.getNom(), client.getPrenom(),
                    client.getAdresse(), client.getTelephone(),
                    client.getEmail(), client.getImage() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la cr�ation du client, aucune ligne ajout�e dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                client.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "�chec de la cr�ation du client en base, aucun ID auto-g�n�r� retourn�." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    @Override
    public List<Client> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Client> clients = new ArrayList<Client>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                clients.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return clients;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    @Override
    public void supprimer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, client.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression du client, aucune ligne supprim�e de la table." );
            } else {
                client.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    /*
     * M�thode g�n�rique utilis�e pour retourner un client depuis la base de
     * donn�es, correspondant � la requ�te SQL donn�e prenant en param�tres les
     * objets pass�s en argument.
     */
    private Client trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Pr�paration de la requ�te avec les objets pass�s en arguments
             * (ici, uniquement un id) et ex�cution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                client = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return client;
    }

    /*
     * Simple m�thode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des clients (un ResultSet) et
     * un bean Client.
     */
    private static Client map( ResultSet resultSet ) throws SQLException {
        Client client = new Client();
        client.setId( resultSet.getLong( "id" ) );
        client.setNom( resultSet.getString( "nom" ) );
        client.setPrenom( resultSet.getString( "prenom" ) );
        client.setAdresse( resultSet.getString( "adresse" ) );
        client.setTelephone( resultSet.getString( "telephone" ) );
        client.setEmail( resultSet.getString( "email" ) );
        client.setImage( resultSet.getString( "image" ) );
        return client;
    }

}