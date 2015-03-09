
import { DataCenter, DataCenterList } from './../model/datacenter.jsx';


export class DashboardPage extends React.Component {

    constructor() {
        this.state = { dataCenterList: new DataCenterList() };

        this
            .state
            .dataCenterList
            .fetch({ success: () => this.onStateChanged() });
    }

    onStateChanged() {
        this.setState(this.state);
    }

    render () {
        return (
            <div className="starter-template">
                <table className="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.dataCenterList.map(item =>
                            <tr>
                                <td>
                                    <a href={`#/${item.get('id')}/server/new`}>{item.get('id')}</a>
                                </td>
                                <td>{item.get('name')}</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        );
    }

}