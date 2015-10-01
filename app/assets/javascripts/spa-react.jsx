var mountNode = document.getElementById('bonmots')

var BonMotItem = React.createClass({
    render: function() {
        return (
            <div className="panel panel-default bonmot">
                <div className="panel-body">
                    <span className="words">{this.props.item.text}</span>
                </div>
                <div className="panel-footer">
                    <div className="small">
                        <span className="pull-right"><em>{moment(this.props.item.createdOn).format('ddd MMM DD HH:mm:ss')}</em></span>
                        <span className="pull-left"><a href={'/user/' + this.props.item.username}>@{this.props.item.username}</a></span>
                        <div className="clearfix"></div>
                    </div>
                </div>
            </div>
        );
    }
});

var BonMotList = React.createClass({
    getInitialState: function() {
      return { items: [] };
    },
    componentDidMount: function() {
        // Call external plugin with injected rjs object
        $.fn.bonmotReact(this);
    },
    render: function() {
        return (
            <div className="bonmot-list">
                {
                    this.state.items.map(function(item) {
                        return <BonMotItem item={item} />
                    })
                }
            </div>
        );
    }
});

React.render(<BonMotList />, mountNode);